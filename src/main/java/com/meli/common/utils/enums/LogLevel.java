package com.meli.common.utils.enums;

/**
 * Enumeración de los niveles de log utilizados en la aplicación.
 *
 * <p>Define varios niveles de log que pueden ser utilizados para clasificar
 * la importancia y el tipo de mensajes que se registran en el sistema.</p>
 *
 * <ul>
 *     <li>{@code ALL} - Utilizado para indicar que todos los mensajes deben ser registrados.</li>
 *     <li>{@code INFO} - Indica mensajes informativos que resaltan el progreso de la aplicación.</li>
 *     <li>{@code WARNING} - Utilizado para situaciones que podrían ser potencialmente dañinas.</li>
 *     <li>{@code ERROR} - Indica situaciones de error que podrían permitir que la aplicación continúe ejecutándose.</li>
 * </ul>
 */
public enum LogLevel {
    ALL,
    INFO,
    WARN,
    ERROR
}
