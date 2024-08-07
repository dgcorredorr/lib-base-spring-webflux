package com.fstech.application.service;

import com.fstech.core.entity.Traceability;

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
