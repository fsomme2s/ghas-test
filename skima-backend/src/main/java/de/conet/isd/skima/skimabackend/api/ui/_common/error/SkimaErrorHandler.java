package de.conet.isd.skima.skimabackend.api.ui._common.error;

import de.conet.isd.skima.skimabackend.api.ui._common.security.CurrentUserInfo;
import de.conet.isd.skima.skimabackend.service._common.error.BusinessException;
import de.conet.isd.skima.skimabackend.service._common.error.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Random;

/**
 * Global Error Handler, mapping Exceptions into proper REST/HTTP Responses so that the UI can process errors correctly.
 */
@ControllerAdvice
public class SkimaErrorHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(SkimaErrorHandler.class);

    private final Random random = new Random(); // Note: Produces Log Ids, no need for Secure Random or similar

    /**
     * Overwrites Default method to include Json Parse Errors in response (e.g. "comma missing in line x")
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // Difference to overridden super-method is: body is not null, but contains the JSON Parse Error Message
        return handleExceptionInternal(ex, ex.getMessage(), headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleInternalError(HttpServletRequest req, Exception ex,
                                                        CurrentUserInfo currentUser
    ) {

        // Exceptions that produce correct HTTP Responses out of the box:
        if (ex instanceof ResponseStatusException statusException) {
            log.warn("statusException thrown with: {}", statusException.getMessage());
            throw statusException;
        }

        if (ex instanceof AuthenticationException authException) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error on Authentication", authException);
        }

        if (ex instanceof AccessDeniedException accessDeniedException) {
            // TODO add log on details of denied request
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Error on Authorization");
        }

        if (ex instanceof ValidationException validationException) {
            log.debug("Validation Error: {}", validationException.getValidationResult().getErrors());
            return new ResponseEntity<>(new ValidationErrorDto(validationException), HttpStatus.BAD_REQUEST);
        }

        if (ex instanceof BusinessException businessException) {
            log.warn("BusinessException for User {}: '{}'", currentUser, businessException.toString());
            return new ResponseEntity<>(new UserErrorDto(businessException), HttpStatus.BAD_REQUEST);
        }

        // Muted Errors:
        if (ex instanceof ClientAbortException clientAbort) {
            log.debug("Client closed request");
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        // Unexpected Errors:
        String errorId = generateErrorId();
        log.error("#{} Uncaught Exception by user {} on '{} {}':",
                errorId, currentUser, req.getMethod(), req.getServletPath(), ex
        );
        return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON)
                .body(new UnexpectedErrorDto(errorId));
    }

    private String generateErrorId() {
        // prefix 6 characters random [A..Z 1..9]
        char[] id = new char[6];
        for (int i = 0; i < id.length; i++) {
            if (random.nextBoolean()) {
                char c = (char) (random.nextInt(26) + 'A');
                if (c == 'O' || c == 'I') c++; // prevent O/0 and I/1 confusion
                id[i] = c; // A + [0..25] => A..Z
            } else {
                id[i] = (char) (random.nextInt(9) + '1'); // 1 + [1..9] => 1..9 - no Zero to avoid O0 confusion
            }
        }
        return new String(id);
    }


    /**
     * Overridden to prevent responses with duplicate error messages in special edge cases.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        if (request instanceof ServletWebRequest req) {
            // This Message occurs when a Response Entity was successfully produced, but the resulting JSON was invalid.
            // Thus, there might already is a partial JSON Response in the response buffer,
            // so we must first clear the response buffer:
            if (req.getResponse() != null) {
                req.getResponse().reset();
            }

            // Now we start the response from scratch with our custom handling:
            String errorId = generateErrorId();
            log.error("#{} Uncaught Exception by on '{} {}':",
                    errorId, req.getHttpMethod(), req.getRequest().getServletPath(), ex);
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON)
                    .body(new UnexpectedErrorDto(errorId));
        }

        return super.handleHttpMessageNotWritable(ex, headers, status, request);
    }

}
