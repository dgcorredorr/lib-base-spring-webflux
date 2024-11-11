package com.meli.application.service;

import com.meli.core.entity.Traceability;

/**
 * La interfaz TraceabilityService define una operación para crear registros de trazabilidad.
 */
public interface TraceabilityService {

    /**
     * Crea un registro de trazabilidad de manera reactiva.
     *
     * @param traceability El objeto Traceability que contiene la información de la trazabilidad.
     */
    void createTraceability(Traceability traceability);
}
