package com.fstech.core.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import org.springframework.http.HttpMethod;

import com.fstech.common.utils.tasks.Task;

/**
 * Clase que representa un registro de error en la aplicación.
 *
 * <p>
 * Esta clase se utiliza para registrar errores que ocurren durante la ejecución
 * de operaciones
 * en la aplicación. Cada registro de error contiene información detallada, como
 * el identificador de
 * transacción, el resultado (éxito o fracaso), el origen del error, el método
 * HTTP utilizado, la tarea
 * relacionada, la clase y el método donde ocurrió el error, el mensaje de
 * error, la traza de la pila,
 * los datos de la solicitud y los datos de respuesta, así como las marcas de
 * tiempo de creación y
 * actualización.
 * </p>
 *
 * <p>
 * El registro de errores es esencial para el diagnóstico y la depuración de
 * problemas en la
 * aplicación, lo que facilita la identificación y corrección de problemas.
 * </p>
 *
 * @see Builder
 * @see Getter
 */
@Getter
@Builder
@ToString
public class ServiceError {
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
}
