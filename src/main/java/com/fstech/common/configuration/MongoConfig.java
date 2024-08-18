package com.fstech.common.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * Configuración de MongoDB para la aplicación.
 *
 * <p>Esta clase de configuración habilita la auditoría en MongoDB, lo que permite
 * el seguimiento automático de ciertos eventos y cambios en los documentos de MongoDB.
 * Utiliza la anotación {@link Configuration} para indicar que es una clase de configuración
 * de Spring y {@link EnableReactiveMongoRepositories} para activar los repositorios reactivos en MongoDB.</p>
 *
 * <p>La auditoría en MongoDB puede incluir el seguimiento de la creación y modificación
 * de documentos, incluyendo la captura de fechas de creación y modificación, y la información
 * del usuario que realizó dichas acciones, si está disponible.</p>
 */
@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.fstech.provider.repository")
@EnableReactiveMongoAuditing
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    private String databaseName;

    @Bean
    @Override
    @NonNull
    public MongoClient reactiveMongoClient() {
        String user = System.getenv("BD_USER");
        String password = System.getenv("BD_PASSWORD");
        String finalUri = mongoUri;

        if (user != null && password != null) {
            finalUri = mongoUri.replace("bd_user", user).replace("bd_password", password);
        }

        // Analizar la URI para obtener el nombre de la base de datos
        ConnectionString connectionString = new ConnectionString(finalUri);
        databaseName = connectionString.getDatabase();

        if (!StringUtils.hasText(databaseName)) {
            throw new IllegalArgumentException("No database name found in URI: " + finalUri);
        }

        return MongoClients.create(finalUri);
    }

    @Override
    @NonNull
    protected String getDatabaseName() {
        return databaseName;
    }
}
