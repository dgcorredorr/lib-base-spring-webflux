package com.fstech.common.utils.enums;

/**
 * Enumeración de tareas específicas para la trazabilidad de solicitudes en la aplicación.
 *
 * <p>Este enum se utiliza para marcar puntos clave en el ciclo de vida de una solicitud,
 * permitiendo un seguimiento detallado y una mejor comprensión del flujo de solicitudes.</p>
 *
 * <ul>
 *     <li>{@code START_REQUEST} - Marca el inicio de una solicitud. Utilizado para iniciar el seguimiento.</li>
 *     <li>{@code REQUEST_ERROR} - Indica que se ha producido un error durante el procesamiento de la solicitud.</li>
 *     <li>{@code END_REQUEST} - Marca la finalización de una solicitud. Utilizado para señalar el fin del seguimiento.</li>
 * </ul>
 */
public enum TraceabilityTask {
    START_REQUEST,
    REQUEST_ERROR,
    END_REQUEST
}
