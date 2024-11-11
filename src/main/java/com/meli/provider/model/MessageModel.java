package com.meli.provider.model;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Clase que representa un modelo de mensaje en la capa de proveedores de la aplicación.
 * <p>
 * Esta clase se utiliza para mapear y almacenar mensajes en la base de datos MongoDB.
 * Contiene campos como el identificador del mensaje, la descripción y el propio mensaje.
 *
 * @see org.springframework.data.annotation.Id
 * @see org.springframework.data.mongodb.core.mapping.Document
 * @see org.springframework.data.mongodb.core.mapping.Field
 */
@Data
@Builder
@Document(collection = "coll_message")
public class MessageModel {
        @Field("id")
        private String messageId;
        private String description;
        private String message;
}
