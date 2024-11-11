package com.meli.provider.model;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.http.HttpMethod;

import com.meli.common.utils.tasks.Task;

import java.time.LocalDateTime;

/**
 * Clase que representa un modelo de error de servicio en la capa de proveedores de la aplicación.
 *
 * Esta clase se utiliza para mapear y almacenar errores de servicio en la base de datos MongoDB.
 * Contiene campos como el identificador de transacción, el éxito de la operación, el origen,
 * el método HTTP, la tarea, la clase de error, el método de error, el mensaje de error, la pila
 * de seguimiento, las solicitudes y respuestas relacionadas, y marcas de tiempo de creación y
 * actualización.
 *
 * @see org.springframework.data.annotation.Id
 * @see org.springframework.data.mongodb.core.mapping.Document
 * @see org.springframework.data.mongodb.core.mapping.Field
 * @see org.springframework.data.annotation.CreatedDate
 * @see org.springframework.data.annotation.LastModifiedDate
 */
@Data
@Builder
@Document(collection = "coll_service_error")
public class ServiceErrorModel {
    @Id
    private String id;
    private String transactionId;
    private Boolean success;
    private String origin;
    private HttpMethod method;
    private Task task;
    private String errorClass;
    private String errorMethod;
    private String message;
    private String stackTrace;
    private Object request;
    private Object response;
    @CreatedDate
    @Field(name = "createdAt")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Field(name = "updatedAt")
    private LocalDateTime updatedAt;
}
