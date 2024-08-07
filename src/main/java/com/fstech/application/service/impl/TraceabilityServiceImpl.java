package com.fstech.application.service.impl;

import org.springframework.stereotype.Service;

import com.fstech.application.service.TraceabilityService;
import com.fstech.core.TraceabilityUseCase;
import com.fstech.core.entity.Traceability;

/**
 * Implementación del servicio para operaciones relacionadas con la trazabilidad.
 *
 * <p>Esta clase proporciona una implementación concreta de {@link TraceabilityService},
 * delegando las operaciones relacionadas con la trazabilidad al caso de uso {@link TraceabilityUseCase}.
 * Se encarga de la lógica de servicio, como la creación de registros de trazabilidad.</p>
 *
 * @see TraceabilityService
 * @see TraceabilityUseCase
 */
@Service
public class TraceabilityServiceImpl implements TraceabilityService {
    private final TraceabilityUseCase traceabilityUseCase;

    /**
     * Constructor para inyectar dependencias del caso de uso de trazabilidad.
     *
     * @param traceabilityUseCase El caso de uso de trazabilidad que maneja la lógica de negocio.
     */
    public TraceabilityServiceImpl(TraceabilityUseCase traceabilityUseCase) {
        this.traceabilityUseCase = traceabilityUseCase;
    }

    /**
     * Crea un registro de trazabilidad de manera reactiva.
     *
     * <p>Este método invoca el caso de uso {@link TraceabilityUseCase} para crear un
     * registro de trazabilidad en el sistema. Es útil para mantener un seguimiento
     * de las actividades o eventos importantes que ocurren durante la ejecución de la aplicación.</p>
     *
     * @param traceability El objeto {@link Traceability} que contiene la información a registrar.
     * @return Un Mono<Void> que indica la finalización de la operación.
     */
    @Override
    public void createTraceability(Traceability traceability) {
        this.traceabilityUseCase.createTraceability(traceability);
    }
}
