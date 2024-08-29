package com.fstech.common.configuration;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configuración general de la aplicación.
 *
 * <p>Esta clase se utiliza para cargar configuraciones generales desde el entorno
 * y ponerlas a disposición en toda la aplicación. Utiliza la anotación {@link Configuration}
 * de Spring para indicar que es una clase de configuración y {@link Value} para inyectar
 * los valores de las propiedades.</p>
 */
@Configuration
public class GeneralConfig {

    @Getter
    private static String logLevel;

    @Getter
    private static String appId;

    @Getter
    private static String appName;

    @Getter
    private static String appDescription;

    @Getter
    private static String appVersion;

    @Value("${app.loglevel}")
    private String logLevelValue;

    @Value("${spring.application.name}")
    private String appIdValue;

    @Value("${app.name}")
    private String appNameValue;

    @Value("${app.description}")
    private String appDescriptionValue;

    @Value("${app.version}")
    private String appVersionValue;

    @PostConstruct
    private void init() {
        GeneralConfig.logLevel = this.logLevelValue;
        GeneralConfig.appId = this.appIdValue;
        GeneralConfig.appName = this.appNameValue;
        GeneralConfig.appDescription = this.appDescriptionValue;
        GeneralConfig.appVersion = this.appVersionValue;
    }
}