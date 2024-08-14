package com.fstech.core.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import org.springframework.http.HttpMethod;

import com.fstech.common.utils.enums.TraceabilityStatus;
import com.fstech.common.utils.tasks.TraceabilityTask;

/**
 * Clase que representa un registro de trazabilidad en la aplicación.
 *
 * <p>Esta clase se utiliza para registrar información de trazabilidad relacionada con las operaciones
 * realizadas en la aplicación. Cada registro de trazabilidad contiene detalles como el identificador
 * de transacción, el estado de la trazabilidad, el origen, el método HTTP utilizado, la tarea relacionada,
 * los datos de solicitud, los datos de respuesta y la duración de la operación en milisegundos.</p>
 *
 * <p>La trazabilidad es esencial para el seguimiento y análisis de las operaciones en la aplicación, lo que
 * permite identificar problemas, evaluar el rendimiento y mejorar la eficiencia.</p>
 *
 * @see Builder
 * @see Getter
 */
@Getter
@Builder
@ToString
public class Traceability {
    private String transactionId;
    private TraceabilityStatus status;
    private String origin;
    private HttpMethod method;
    private TraceabilityTask task;
    private Object request;
    private Object response;
    private Long durationMillis;
}
