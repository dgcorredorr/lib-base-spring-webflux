package com.meli.provider.impl;

import org.springframework.stereotype.Component;

import com.meli.core.entity.Traceability;
import com.meli.provider.TraceabilityProvider;
import com.meli.provider.mapper.TraceabilityMapper;
import com.meli.provider.repository.TraceabilityRepository;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Span;

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
        Span span = ElasticApm.currentSpan().startSpan("db", "mongodb", "save");
        span.setName("MongoDB Save Traceability " + traceability.getTask());
        traceabilityRepository.save(traceabilityMapper.toModel(traceability))
                .doOnError(Exception.class, span::captureException)
                .doFinally(signalType -> span.end())
                .subscribe();
    }
}
