package com.fstech.core;

import com.fstech.core.entity.Traceability;
import reactor.core.publisher.Mono;

/**
 * La interfaz TraceabilityUseCase define operaciones para gestionar registros de trazabilidad en la aplicación.
 */
public interface TraceabilityUseCase {

    /**
     * Crea un registro de trazabilidad de manera reactiva.
     *
     * @param traceability El objeto Traceability que contiene la información de la trazabilidad.
     * @return Un Mono<Void> que indica la finalización de la operación.
     */
    Mono<Void> createTraceability(Traceability traceability);
}
