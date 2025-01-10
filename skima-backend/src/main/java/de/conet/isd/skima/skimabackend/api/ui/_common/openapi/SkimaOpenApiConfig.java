package de.conet.isd.skima.skimabackend.api.ui._common.openapi;

import de.conet.isd.skima.skimabackend.api.ui._common.error.*;
import de.conet.isd.skima.skimabackend.service._common.config.AppConfig;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * Global Configuration
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Skill Manager Swagger UI"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class SkimaOpenApiConfig {

    //
    // Static config (because it's not provided via config properties)
    //
    static {
        io.swagger.v3.core.jackson.ModelResolver.enumsAsRef = true;
    }

    //
    // Programmatic config:
    //
    /**
     * @param transitiveDtos any other dtos nested within your errorDto class
     */
    private record ErrorResponseDefinition(int statusCode, String description, Class<?> errorDto, Class<?>... transitiveDtos) {
    }

    // TODO add @Operation(security = @SecurityRequirement(name = "bearerAuth")) to all operations
    //  except those in BuWebSecurityConfig.PUBLIC_URLS_WHITELIST

    /**
     * key = status code, value = pair of (dto class, description)
     */
    private static final Set<ErrorResponseDefinition> statusCodeDtoMap = Set.of(
            new ErrorResponseDefinition(500, "Internal Server Error", UnexpectedErrorDto.class
            ),
            new ErrorResponseDefinition(400, "Error by User / Client; look at .type property of" +
                                             " returned error object on how to handle it!", UserErrorDto.class
            ), // TODO: mapping several response types to same statusId requires "schema: oneOf: -$ref ... - $ref ... "
            new ErrorResponseDefinition(400, "Error on Form / Input Validation - usually, the UI's" +
                                             " form validation should have prevented this error to happen " +
                                             " in the first place", ValidationErrorDto.class, FieldValidationErrorDto.class
            )
    );


    /**
     * Adds Common (Error) Responses to all the resources of our API.
     */
    @Bean
    public OpenApiCustomizer openApiCustomizer(AppConfig appConfig) {
        return openApi ->
        {
            openApi.getInfo().setVersion(appConfig.version());

            // Add Exception Dtos to Schemas (necessary, because they are not directly used in any endpoints):
            addSchema(openApi, ErrorDto.class);

            for (ErrorResponseDefinition errorResponseDefinition : statusCodeDtoMap) {
                addSchema(openApi, errorResponseDefinition.errorDto);
                for (Class<?> nestedDtos : errorResponseDefinition.transitiveDtos()) {
                    addSchema(openApi, nestedDtos);
                }
            }


            // Add them to all the endpoints (think of it as "OpenApi's Reflection"):
            // that's basically a workaround because there is no way to add "global response types" in OpenApi yet.
            for (PathItem pathItem : openApi
                    .getPaths()
                    .values()) {
                for (Operation operation : pathItem
                        .readOperations()) {
                    for (ErrorResponseDefinition errorResponseDefinition : statusCodeDtoMap) {
                        operation.getResponses().addApiResponse(
                                String.valueOf(errorResponseDefinition.statusCode),
                                new ApiResponse()
                                        .content(toSchemaRefContent(errorResponseDefinition.errorDto))
                                        .description(errorResponseDefinition.description)
                        );
                    }
                }
            }
        };
    }

    private void addSchema(OpenAPI openApi, Class<?> dtoClass) {
        openApi.getComponents().addSchemas(dtoClass.getSimpleName(), toSchema(dtoClass));
    }

    private Content toSchemaRefContent(Class<?> dtoClass) {
        return new Content().addMediaType("application/json",
                new MediaType().schema(new Schema<>().$ref("#/components/schemas/" + dtoClass.getSimpleName())));
    }

    private Schema<?> toSchema(Class<?> dtoClass) {
        return ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(dtoClass)).schema;
    }

    /**
     * Add customizer for swagger-ui index.html
     */
    @Bean
    public SwaggerIndexTransformer swaggerIndexTransformer(
            SwaggerUiConfigProperties a,
            SwaggerUiOAuthProperties b,
            SwaggerUiConfigParameters c,
            SwaggerWelcomeCommon d,
            ObjectMapperProvider e
    ) {
        return new SwaggerCodeBlockTransformer(a, b, c, d, e);
    }
}
