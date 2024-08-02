package com.fstech.application.service;

import com.fstech.core.entity.Traceability;
import reactor.core.publisher.Mono;

/**
 * La interfaz TraceabilityService define una operación para crear registros de trazabilidad.
 */
public interface TraceabilityService {

    /**
     * Crea un registro de trazabilidad de manera reactiva.
     *
     * @param traceability El objeto Traceability que contiene la información de la trazabilidad.
     * @return Un Mono<Void> que indica la finalización de la operación.
     */
    Mono<Void> createTraceability(Traceability traceability);
}
