package com.meli.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Estructura general utilizada por el servicio para recibir una solicitud.
 *
 * <p>Esta clase representa un formato de solicitud genérico utilizado en la aplicación.
 * Contiene información básica que es comúnmente incluida en las solicitudes,
 * como documentos asociados con la solicitud.</p>
 *
 * <p>Se utiliza para asegurar una estructura de solicitud coherente a través de diferentes
 * partes de la aplicación, facilitando la estandarización y el manejo de las solicitudes
 * en el lado del servidor.</p>
 *
 * @see Serializable
 */
@Data
@Schema(description = "Estructura de petición genérica para las solicitudes de la API.")
public class GenericRequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -7868182870673334153L;

    @Schema(description = "Documentos asociados con la solicitud.")
    @NotNull
    @NotEmpty
    private transient List<Map<String, Object>> documents;
}
