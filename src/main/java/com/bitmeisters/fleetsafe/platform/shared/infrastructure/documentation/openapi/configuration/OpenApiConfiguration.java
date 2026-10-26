package com.bitmeisters.fleetsafe.platform.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${documentation.application.description}")
    String applicationDescription;

    @Value("${documentation.application.version}")
    String applicationVersion;

    @Bean
    public OpenAPI fleetSafePlatformOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName + " API")
                        .description(applicationDescription)
                        .version(applicationVersion)
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("FleetSafe Platform repository")
                        .url("https://github.com/1ASI0729-2620-16692-BitMeisters/Web-Services"));
    }
}
