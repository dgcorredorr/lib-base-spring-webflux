package com.meli.common.utils.enums;

/**
 * Enumeración de los códigos de mensajes utilizados en la aplicación.
 *
 * <p>Define varios códigos de mensajes estándar que se utilizan para representar
 * diferentes tipos de respuestas y situaciones en la aplicación. Estos códigos
 * facilitan la estandarización de los mensajes.</p>
 *
 * <ul>
 *     <li>{@code DEFAULT_SUCCESS} - Representa un mensaje genérico de operación exitosa.</li>
 *     <li>{@code DEFAULT_ERROR} - Indica un mensaje genérico de error en la operación.</li>
 *     <li>{@code VALIDATION_FIELD_ERROR} - Utilizado para errores de validación en campos de entrada.</li>
 *     <li>{@code MESSAGE_NOT_FOUND} - Indica que un mensaje específico no se encontró.</li>
 *     <li>{@code PARAM_NOT_FOUND} - Utilizado cuando un parámetro esperado no se encuentra en base de datos.</li>
 * </ul>
 */
public enum MessageMapping {
    DEFAULT_SUCCESS,
    DEFAULT_ERROR,
    VALIDATION_FIELD_ERROR,
    MESSAGE_NOT_FOUND,
    BAD_CREDENTIALS,
    NO_CREDENTIALS
}
