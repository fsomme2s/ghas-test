package de.conet.isd.skima.skimabackend.service._common.error;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception for Generic Domain Errors, that cannot be further handled by the backend but should be
 * <b>displayed to the end user.</b>
 *
 * <p>
 *  <b>Don't confuse with {@link ValidationException}s</b> used to validate Form Input!
 * </p>
 * <p>
 *  Usually this happens when some Business logic is violated and usually the UI should prevent those "user errors"
 *  in the first place (e.g. by disabling buttons you cannot click etc.). But if the UI fails to prevent this or the
 *  business rules are too complex to be checked in the ui, then this exception type will hit the User and be displayed
 *  in a defined way.
 * </p>
 */
public class BusinessException extends RuntimeException {
    public enum ERROR_CODE {
        /**
         * Used for testing / demoing the error mechanism
         * <p>data: <ul>
         *     <li><b>.example</b>: int, random value</li>
         *     <li><b>.example2</b>: string, contains a greeting</li>
         * </ul></p>
         */
        DEMO,

        /**
         * Generic Duplicate Exception: A Unique Constraint Violation (e.g.: title/name/id/... already exists).
         * Usually this means: per owner;
         */
        ALREADY_EXISTS
    }

    //
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //

    /**
     * Error Code for this exception type
     */
    private final ERROR_CODE errorCode;

    /**
     * Data, if additional data is required for this {@link #errorCode}.
     * See Doc on the individual error codes for info on whether to expect data and which keys with which types are
     * used.
     */
    private final Map<String, Object> data = new HashMap<>();

    public BusinessException(ERROR_CODE errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ERROR_CODE errorCode, String message, Map<String, Object> data) {
        super(message);
        this.errorCode = errorCode;
        this.data.putAll(data);
    }

    public BusinessException(ERROR_CODE errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BusinessException(ERROR_CODE errorCode, String message, Throwable cause, Map<String, Object> data) {
        super(message, cause);
        this.errorCode = errorCode;
        this.data.putAll(data);
    }

    public ERROR_CODE getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "%s: %s; data=%s".formatted(errorCode, getMessage(), data);
    }
}
