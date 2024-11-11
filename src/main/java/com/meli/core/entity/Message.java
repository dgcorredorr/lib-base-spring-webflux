package com.meli.core.entity;

import lombok.Builder;
import lombok.Getter;

/**
 * Clase que representa un mensaje con identificador único, descripción y contenido.
 *
 * <p>Esta clase se utiliza para almacenar y manipular mensajes dentro de la aplicación.
 * Cada mensaje consta de tres partes principales: un identificador único, una descripción
 * que proporciona información adicional sobre el mensaje y el contenido del propio mensaje.</p>
 *
 * <p>El identificador único es utilizado para identificar el mensaje de manera única en la
 * aplicación, la descripción proporciona contexto sobre el mensaje y el contenido del mensaje
 * puede ser cualquier información relevante.</p>
 *
 * @see Builder
 * @see Getter
 */
@Getter
@Builder
public class Message {
    private String messageId;
    private String description;
    private String messageContent;
}
