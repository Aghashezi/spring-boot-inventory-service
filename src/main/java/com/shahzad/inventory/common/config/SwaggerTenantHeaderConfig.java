package com.shahzad.inventory.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerTenantHeaderConfig {
    @Bean
    public OpenApiCustomizer tenantHeaderOpenApiCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
            pathItem.readOperations().forEach(operation ->
                operation.addParametersItem(new Parameter()
                    .in("header")
                    .name("X-Tenant-Id")
                    .description("Tenant identifier (required for all requests)")
                    .required(true)
                    .example("tenant1")
                )
            )
        );
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("basicAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic")
                        .description("Basic authentication with username:password. Use 'admin:admin' for GLOBAL_ADMIN or 'user:password' for USER role.")
                )
            )
            .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }
}
