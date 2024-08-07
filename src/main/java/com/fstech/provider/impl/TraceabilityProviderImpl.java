package com.fstech.provider.impl;

import org.springframework.stereotype.Component;

import com.fstech.core.entity.Traceability;
import com.fstech.provider.TraceabilityProvider;
import com.fstech.provider.mapper.TraceabilityMapper;
import com.fstech.provider.repository.TraceabilityRepository;

/**
 * Implementación de la interfaz {@link TraceabilityProvider} que proporciona operaciones para registrar trazabilidad.
 *
 * <p>Esta clase se encarga de interactuar con el repositorio de trazabilidad y utiliza un mapeador para convertir entre
 * entidades del repositorio y objetos de dominio de trazabilidad. También mide el tiempo de ejecución de la operación y
 * registra eventos relacionados con la trazabilidad.</p>
 *
 * @see TraceabilityProvider
 */
@Component
public class TraceabilityProviderImpl implements TraceabilityProvider {
    private final TraceabilityRepository traceabilityRepository;
    private final TraceabilityMapper traceabilityMapper;

    public TraceabilityProviderImpl(TraceabilityRepository traceabilityRepository, TraceabilityMapper traceabilityMapper) {
        this.traceabilityRepository = traceabilityRepository;
        this.traceabilityMapper = traceabilityMapper;
    }

    @Override
    public void createTraceability(Traceability traceability) {
        traceabilityRepository.save(traceabilityMapper.toModel(traceability)).subscribe();
    }
}
