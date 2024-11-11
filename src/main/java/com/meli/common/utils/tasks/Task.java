package com.meli.common.utils.tasks;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa una tarea específica realizada en la aplicación.
 */
@Getter
@Setter
public class Task {

    private final String id;
    private final String description;
    private Origin origin;

    public Task(String id, String description) {
        this.id = id;
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
     * Representación en cadena de la tarea.
     *
     * @return Una cadena que contiene el ID, descripción y origen de la tarea.
     */
    @Override
    public String toString() {
        return "Task { " +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", origin=" + getOriginString() +
                " }";
    }

    /**
     * Obtiene una representación en cadena del origen de la tarea.
     *
     * @return Una cadena que describe el origen de la tarea, o "No origin set" si
     *         no hay origen.
     */
    public String getOriginString() {
        if (origin != null) {
            return "{ class: \"" + origin.getOriginClass() + "\", method: \"" + origin.getOriginMethod() + "\" }";
        }
        return "No origin set";
    }
}
