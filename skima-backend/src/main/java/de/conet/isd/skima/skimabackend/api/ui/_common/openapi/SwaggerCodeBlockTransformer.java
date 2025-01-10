package de.conet.isd.skima.skimabackend.api.ui._common.openapi;

import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;

import java.io.IOException;

public class SwaggerCodeBlockTransformer extends SwaggerIndexPageTransformer {

    public SwaggerCodeBlockTransformer(SwaggerUiConfigProperties swaggerUiConfig,
                                       SwaggerUiOAuthProperties swaggerUiOAuthProperties,
                                       SwaggerUiConfigParameters swaggerUiConfigParameters,
                                       SwaggerWelcomeCommon swaggerWelcomeCommon,
                                       ObjectMapperProvider objectMapperProvider
    ) {
        super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerUiConfigParameters, swaggerWelcomeCommon, objectMapperProvider);
    }

    @Override
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformer) throws IOException {
        // Currently not used, because to complicated / hacky;
        // Initial Intention was to create a custom login mechanism for localhost on swagger-ui,
        // but using long token lifetime locally + persistAuthorization=true is good enough
        //
        // Usage Example:
        //        if (resource.toString().contains("swagger-ui.css")) {
        //            final InputStream is = resource.getInputStream();
        //            final InputStreamReader isr = new InputStreamReader(is);
        //            try (BufferedReader br = new BufferedReader(isr)) {
        //                final String css = br.lines().collect(Collectors.joining());
        //                final byte[] transformedContent = css.replace("old", "new").getBytes();
        //                return new TransformedResource(resource, transformedContent);
        //            } // AutoCloseable br > isr > is
        //        }
        return super.transform(request, resource, transformer);
    }

}
