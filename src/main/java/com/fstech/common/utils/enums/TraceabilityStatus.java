package com.fstech.common.utils.enums;

/**
 * Enumeración de los estados de trazabilidad utilizados en la aplicación.
 *
 * <p>Define los diferentes estados que pueden ser asignados a una operación o proceso
 * en el sistema para indicar su resultado y facilitar la trazabilidad y el análisis.</p>
 *
 * <ul>
 *     <li>{@code FAILED} - Indica que la operación o proceso ha fallado.</li>
 *     <li>{@code ERROR} - Representa un error durante la operación o proceso.</li>
 *     <li>{@code SUCCESS} - Denota un éxito en la operación o proceso.</li>
 *     <li>{@code LEGACY_SUCCESS} - Indica un éxito en una operación o proceso dentro de un sistema legado.</li>
 *     <li>{@code LEGACY_ERROR} - Representa un error en una operación o proceso dentro de un sistema legado.</li>
 *     <li>{@code LEGACY_WARN} - Utilizado para advertencias relacionadas con operaciones o procesos en sistemas legados.</li>
 * </ul>
 */
public enum TraceabilityStatus {
    FAILED,
    ERROR,
    SUCCESS,
    LEGACY_SUCCESS,
    LEGACY_ERROR,
    LEGACY_WARN,
}
