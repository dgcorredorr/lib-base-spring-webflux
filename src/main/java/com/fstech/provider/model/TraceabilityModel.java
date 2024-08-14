package com.fstech.provider.model;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpMethod;

import com.fstech.common.utils.enums.TraceabilityStatus;
import com.fstech.common.utils.tasks.TraceabilityTask;

import java.time.LocalDateTime;

/**
 * Clase que representa un modelo de trazabilidad en la capa de proveedores de la aplicación.
 *
 * Esta clase se utiliza para mapear y almacenar registros de trazabilidad en la base de datos MongoDB.
 * Contiene campos como el identificador de transacción, el estado de trazabilidad, el origen, el método HTTP,
 * la tarea de trazabilidad, la solicitud y respuesta relacionadas, la duración en milisegundos,
 * y marcas de tiempo de creación y actualización.
 *
 * @see org.springframework.data.annotation.Id
 * @see org.springframework.data.mongodb.core.mapping.Document
 * @see org.springframework.data.annotation.CreatedDate
 * @see org.springframework.data.annotation.LastModifiedDate
 */
@Data
@Builder
@Document(collection = "coll_traceability")
public class TraceabilityModel {
    @Id
    private String id;
    private String transactionId;
    private TraceabilityStatus status;
    private String origin;
    private HttpMethod method;
    private TraceabilityTask task;
    private Object request;
    private Object response;
    private Long durationMillis;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
