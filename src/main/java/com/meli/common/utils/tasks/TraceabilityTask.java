package com.meli.common.utils.tasks;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa una tarea específica para la trazabilidad de solicitudes
 * en la aplicación.
 *
 * <p>
 * Esta clase se utiliza para marcar puntos clave en el ciclo de vida de una
 * solicitud,
 * permitiendo un seguimiento detallado y una mejor comprensión del flujo de
 * solicitudes.
 * </p>
 */
@Getter
@Setter
public class TraceabilityTask {

    private final String id;
    private String description;

    public TraceabilityTask(String id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Representación en cadena de la tarea.
     *
     * @return Una cadena que contiene el ID y descripción de la tarea.
     */
    @Override
    public String toString() {
        return "TraceabilityTask { " +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                " }";
    }
}
