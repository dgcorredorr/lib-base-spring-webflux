package com.fstech.common.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para la documentación de la API del microservicio.
 *
 * <p>Esta clase configura Swagger/OpenAPI para generar la documentación de la API,
 * facilitando la visualización y prueba de los endpoints del microservicio. Define
 * la información básica como el título, la descripción, la versión y la licencia
 * de la API.</p>
 *
 * @see Configuration
 * @see OpenAPI
 */
@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    /**
     * Crea y configura el bean de OpenAPI para la documentación de la API.
     *
     * <p>Este método configura la información básica de la API, incluyendo el título,
     * la descripción, la versión y los detalles de la licencia. Esta configuración
     * es utilizada por herramientas como Swagger UI para generar la documentación
     * interactiva de la API.</p>
     *
     * @return Un objeto {@link OpenAPI} configurado con la información de la API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(GeneralConfig.getAppName())
                        .description(GeneralConfig.getAppDescription())
                        .version(GeneralConfig.getAppVersion())
                        .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                );
    }
}

