package de.conet.isd.skima.skimabackend.api.ui.system;

import de.conet.isd.skima.skimabackend.api.ui._common.Paths;
import de.conet.isd.skima.skimabackend.api.ui._common.error.ErrorDto;
import de.conet.isd.skima.skimabackend.api.ui.system.dto.StatusDto;
import de.conet.isd.skima.skimabackend.service._common.config.AppConfig;
import de.conet.isd.skima.skimabackend.service._common.error.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static de.conet.isd.skima.skimabackend.domain.entities.users.SkimaPermissionConstants.DEMO_POC;

/**
 * Api for System Status & technical tests / pocs
 */
@RestController
@RequestMapping(Paths.BASE_PATH + "status")
public class StatusApi {
    private final Logger log = LoggerFactory.getLogger(StatusApi.class);

    private final AppConfig appConfig;

    public StatusApi(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GetMapping
    // Security: Whitelisted Endpoint
    public StatusDto getStatus() {
        log.debug("Called Status Api");
        return new StatusDto(appConfig.env(), appConfig.version(), appConfig.versionTimestamp());
    }

    /**
     * Call this method to test how Server Errors are handled.
     *
     * @param type Which Error type to provoke; {@link ErrorDto}.type values can be used.
     *             If the type is unkonwn, a Bad Request with technical error description will be thrown.
     */
    @GetMapping(path = "provoke-error")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('" + DEMO_POC + "')")
    public void provokeError(@RequestParam(defaultValue = ErrorDto.UNEXPECTED) String type) {
        switch (type) {
            case ErrorDto.UNEXPECTED -> throw new NullPointerException("Just a test exception!");
            case ErrorDto.USER -> throw new BusinessException(
                    BusinessException.ERROR_CODE.DEMO,
                    "Provoked Error for Demo purpose",
                    Map.of(
                            "example", (int) (Math.random() * 100),
                            "example2", "Hello Error!"
                    )
            );
//            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unkonwn error type: " + type);
        }
    }

}
