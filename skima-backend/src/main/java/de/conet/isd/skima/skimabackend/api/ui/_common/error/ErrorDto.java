package de.conet.isd.skima.skimabackend.api.ui._common.error;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Base for Error Dtos, meant to be processed by our UI. Field "type" shows which specific error type this is.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UnexpectedErrorDto.class, name = ErrorDto.UNEXPECTED),
    @JsonSubTypes.Type(value = UserErrorDto.class, name = ErrorDto.USER),
    @JsonSubTypes.Type(value = ValidationErrorDto.class, name = ErrorDto.VALIDATION),
})
// Open Api doc - keep consistent to Jackson's @JsonSubTypes (sorry, no better integration exists yet)
@Schema(
    discriminatorProperty = "type",
    oneOf = {UnexpectedErrorDto.class, UserErrorDto.class},
    discriminatorMapping = {
            @DiscriminatorMapping(value = ErrorDto.UNEXPECTED, schema = UnexpectedErrorDto.class),
            @DiscriminatorMapping(value = ErrorDto.USER, schema = UserErrorDto.class),
            @DiscriminatorMapping(value = ErrorDto.VALIDATION, schema = ValidationErrorDto.class)
    }
)
public abstract class ErrorDto {
    public static final String UNEXPECTED = "UNEXPECTED";
    public static final String USER = "USER";
    public static final String VALIDATION = "VALIDATION";
}
