package com.fstech.common.utils.enums;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Enumeración de tareas específicas realizadas en la aplicación.
 *
 * <p>Este enum se utiliza para identificar y describir distintas tareas
 * que se llevan a cabo en el sistema, proporcionando una manera estandarizada
 * de referenciar estas acciones en logs, trazabilidad y manejo de errores.</p>
 *
 * <p>Cada tarea tiene una descripción asociada y puede tener un {@link Origin}
 * para detallar información adicional sobre el origen de la tarea.</p>
 */
@Getter
public enum Task {

    CREATE_SERVICE_ERROR("Create Service Error"),
    CREATE_TRACEABILITY("Create Traceability"),
    EXCEPTION_MANAGER( "Exception Manager"),
    GET_MESSAGE("Get mensaje"),
    INIT_MICROSERVICE("Init Service"), 
    HTTP_REQUEST_FILTER("HTTP Request Filter"), 
    HTTP_RESPONSE_FILTER("HTTP Response Filter"), 
    TEST_TASK("Test Task");

    private final String description;

    @Setter
    private Origin origin;

    Task(String description) {
        this.description = description;
    }

    /**
     * Clase interna que representa el origen de una tarea.
     * Contiene detalles sobre la clase y el método de origen.
     */
    @Builder
    @Getter
    public static class Origin {
        private final String originClass;
        private final String originMethod;
    }

    /**
     * Obtiene una representación en cadena del origen de la tarea.
     *
     * @return Una cadena que describe el origen de la tarea, o una cadena vacía si no hay origen.
     */
    public String getOriginString() {
        if (origin != null) {
            return "{ class: \"" + origin.getOriginClass() + "\", method: \"" + origin.getOriginMethod() + "\" }";
        }
        return null;
    }

}

