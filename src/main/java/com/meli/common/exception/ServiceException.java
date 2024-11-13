package com.meli.common.exception;

import org.springframework.http.HttpStatus;

import com.meli.common.utils.tasks.Task;

import lombok.Getter;

/**
 * Excepción base para representar errores en el contexto de los servicios de la
 * aplicación.
 *
 * <p>
 * Esta clase de excepción se utiliza para indicar condiciones de error en el
 * ámbito
 * de los servicios, incluyendo fallos en la lógica de negocio, problemas de
 * integración,
 * y otros errores relacionados con la operación de los servicios.
 * </p>
 *
 * <p>
 * Además de proporcionar un mensaje de error y una causa, esta excepción puede
 * llevar
 * información adicional sobre la tarea específica que se estaba ejecutando
 * cuando ocurrió
 * el error y la clase de excepción que lo originó.
 * </p>
 *
 * @see RuntimeException
 */
@Getter
public class ServiceException extends RuntimeException {

    private final Class<?> exceptionClass;
    private final HttpStatus httpStatus;
    private final transient Task task;
    private final transient Object documents;

    /**
     * Construye una nueva excepción de servicio con el mensaje de error, una causa
     * subyacente,
     * una tarea específica y la clase de excepción.
     *
     * @param message        El mensaje detallado de la excepción.
     * @param code           El código de estado HTTP asociado con la excepción.
     * @param cause          La causa de la excepción.
     * @param task           La tarea específica durante la cual ocurrió la
     *                       excepción.
     * @param exceptionClass La clase de excepción que originó el error.
     */
    public ServiceException(String message, HttpStatus code, Throwable cause, Task task, Class<?> exceptionClass, Object documents) {
        super(message, cause);
        this.httpStatus = code;
        this.exceptionClass = exceptionClass;
        this.task = task;
        this.documents = documents;
    }
}
