package com.meli.core;

import com.meli.core.entity.Traceability;

/**
 * La interfaz TraceabilityUseCase define operaciones para gestionar registros de trazabilidad en la aplicación.
 */
public interface TraceabilityUseCase {

    /**
     * Crea un registro de trazabilidad de manera reactiva.
     *
     * @param traceability El objeto Traceability que contiene la información de la trazabilidad.
     */
    void createTraceability(Traceability traceability);
}
