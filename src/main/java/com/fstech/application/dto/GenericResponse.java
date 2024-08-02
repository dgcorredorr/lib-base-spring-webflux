package com.fstech.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Estructura general utilizada por el servicio para entregar una respuesta.
 *
 * <p>Esta clase representa un formato de respuesta genérico utilizado en la aplicación.
 * Contiene información básica que es comúnmente incluida en las respuestas a las solicitudes,
 * como el estado de éxito, el origen de la respuesta y un mensaje descriptivo.</p>
 *
 * <p>Se utiliza para asegurar una estructura de respuesta coherente a través de diferentes
 * partes de la aplicación, facilitando la estandarización y el manejo de las respuestas
 * en el lado del cliente.</p>
 *
 * @see Serializable
 */
@Data
@Builder
@Schema(description = "Estructura de respuesta genérica para las solicitudes de la API.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -7868182870673334153L;

    @Schema(description = "Indica si la solicitud fue exitosa o no.")
    private boolean success;

    @Schema(description = "Origen de la respuesta, puede ser utilizado para identificar el componente o parte de la aplicación que generó la respuesta.")
    private String origin;

    @Schema(description = "Mensaje descriptivo asociado con la respuesta. Puede contener información adicional sobre el resultado de la solicitud.")
    private String message;

    @Schema(description = "Documentos asociados con la respuesta.")
    private Object documents;

    @Schema(description = "Fecha y hora en que se creó la respuesta.")
    private LocalDateTime timestamp;

    @Schema(description = "Detalles adicionales sobre el error, si los hay.")
    @JsonIgnore
    private Map<String, Object> errorDetails;

    @Schema(description = "ID de la solicitud asociado con esta respuesta.")
    private String requestId;
}
