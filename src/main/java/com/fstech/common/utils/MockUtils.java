package com.fstech.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class MockUtils {

    private static final Logger logger = LoggerFactory.getLogger(MockUtils.class);

    @Value("${mock.json.path:null}")
    private String jsonFilePath;

    private static Map<String, Object> mockData;

    // Constructor privado para evitar instanciación
    private MockUtils() {}

    @PostConstruct
    private void init() {
        loadMockData(jsonFilePath);
    }

    /**
     * Carga el archivo JSON desde el classpath y lo deserializa en un Map.
     *
     * @param jsonFilePath la ruta del archivo JSON en el classpath
     */
    private static void loadMockData(String jsonFilePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (!jsonFilePath.equals("null")) {
            try {
                mockData = objectMapper.readValue(
                    MockUtils.class.getResourceAsStream(jsonFilePath),
                    new TypeReference<Map<String, Object>>() {}
                );
                logger.info("Archivo JSON cargado correctamente desde {}", jsonFilePath);
            } catch (IOException e) {
                logger.error("Error al cargar el archivo JSON desde {}: {}", jsonFilePath, e.getMessage());
                mockData = Collections.emptyMap();
            }
        } else {
            mockData = Collections.emptyMap();
        }
        
    }

    /**
     * Obtiene el valor asociado a una clave en el archivo JSON cargado.
     *
     * @param key la clave del valor que se desea obtener
     * @return el valor asociado a la clave, o null si no existe la clave
     */
    public static Object getMockValue(String key) {
        return mockData.get(key);
    }

    /**
     * Devuelve el mapa completo cargado desde el archivo JSON.
     *
     * @return un Map<String, Object> con los datos del JSON
     */
    public static Map<String, Object> getMockData() {
        return mockData;
    }
}
