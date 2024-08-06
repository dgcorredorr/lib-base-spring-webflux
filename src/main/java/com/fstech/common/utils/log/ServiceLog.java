package com.fstech.common.utils.log;

import com.fstech.common.utils.enums.LogLevel;
import com.fstech.common.utils.enums.Task;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * Clase que representa un registro de servicio con varios atributos detallados.
 *
 * <p>Esta clase se utiliza para almacenar y manipular registros de servicio dentro de la aplicación.
 * Cada registro de servicio consta de varios atributos principales: el nombre de la aplicación,
 * la tarea asociada, una descripción de la tarea, un identificador de transacción, un mensaje,
 * el origen del log, el nivel del log, un objeto y el tiempo de procesamiento.</p>
 *
 * <p>El nombre de la aplicación identifica la aplicación que genera el log, la tarea proporciona
 * contexto sobre la actividad registrada, la descripción de la tarea proporciona información adicional,
 * el identificador de transacción identifica de manera única la transacción, el mensaje contiene
 * información relevante sobre el evento registrado, el origen del log indica de dónde proviene el log,
 * el nivel del log especifica la severidad del evento, el objeto puede contener cualquier información
 * adicional relevante y el tiempo de procesamiento mide la duración del procesamiento en milisegundos.</p>
 *
 * @see Builder
 * @see Getter
 */
@Data
@Builder
public class ServiceLog {
    private String applicationName;
    private Task task;
    private String taskDescription;
    private String transactionId;
    private String message;
    private String logOrigin;
    private LogLevel level;
    private Object object;
    private Long processingTime;
}
