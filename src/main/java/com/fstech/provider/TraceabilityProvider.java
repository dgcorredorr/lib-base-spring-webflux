package com.fstech.provider;

import com.fstech.core.entity.Traceability;
import reactor.core.publisher.Mono;

/**
 * La interfaz TraceabilityProvider define métodos para registrar la trazabilidad de acciones en la aplicación.
 * Proporciona una operación para crear un registro de trazabilidad.
 */
public interface TraceabilityProvider {

    /**
     * Crea un registro de trazabilidad para una acción en el sistema de manera reactiva.
     *
     * @param traceability El objeto Traceability que contiene información sobre la trazabilidad.
     */
    void createTraceability(Traceability traceability);
}
