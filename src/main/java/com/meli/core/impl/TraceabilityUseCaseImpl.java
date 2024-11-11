package com.meli.core.impl;

import org.springframework.stereotype.Service;

import com.meli.core.TraceabilityUseCase;
import com.meli.core.entity.Traceability;
import com.meli.provider.TraceabilityProvider;

/**
 * Implementación de la interfaz {@link TraceabilityUseCase} que proporciona la lógica
 * para la creación y registro de trazabilidad en la aplicación.
 *
 * <p>Esta clase se encarga de interactuar con un proveedor de trazabilidad para
 * crear y registrar información de trazabilidad en la aplicación.</p>
 */
@Service
public class TraceabilityUseCaseImpl implements TraceabilityUseCase {
    private final TraceabilityProvider traceabilityProvider;

    /**
     * Constructor de la clase que recibe una instancia de {@link TraceabilityProvider}
     * para la gestión de la trazabilidad.
     *
     * @param traceabilityProvider El proveedor de trazabilidad utilizado para crear y
     *                             registrar información de trazabilidad en la aplicación.
     */
    public TraceabilityUseCaseImpl(TraceabilityProvider traceabilityProvider) {
        this.traceabilityProvider = traceabilityProvider;
    }

    @Override
    public void createTraceability(Traceability traceability) {
        this.traceabilityProvider.createTraceability(traceability);
    }
}
