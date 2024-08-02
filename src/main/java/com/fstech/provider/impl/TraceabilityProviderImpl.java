package com.fstech.provider.impl;

import org.springframework.stereotype.Component;

import com.fstech.common.utils.ServiceLogger;
import com.fstech.common.utils.enums.LogLevel;
import com.fstech.common.utils.enums.Task;
import com.fstech.core.entity.Traceability;
import com.fstech.provider.TraceabilityProvider;
import com.fstech.provider.mapper.TraceabilityMapper;
import com.fstech.provider.repository.TraceabilityRepository;

import reactor.core.publisher.Mono;

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
    private final ServiceLogger<TraceabilityProviderImpl> logger;
    private final TraceabilityRepository traceabilityRepository;
    private final TraceabilityMapper traceabilityMapper;

    public TraceabilityProviderImpl(ServiceLogger<TraceabilityProviderImpl> logger, TraceabilityRepository traceabilityRepository, TraceabilityMapper traceabilityMapper) {
        logger.setLoggerClass(TraceabilityProviderImpl.class);
        this.logger = logger;
        this.traceabilityRepository = traceabilityRepository;
        this.traceabilityMapper = traceabilityMapper;
    }

    @Override
    public Mono<Void> createTraceability(Traceability traceability) {
        long startTime = System.currentTimeMillis();

        return traceabilityRepository.save(traceabilityMapper.toModel(traceability))
                .doOnSuccess(savedTraceability -> {
                    long endTime = System.currentTimeMillis();
                    long executionTime = endTime - startTime;

                    Task task = Task.CREATE_TRACEABILITY;
                    task.setOrigin(Task.Origin.builder()
                            .originClass("TraceabilityProviderImpl")
                            .originMethod("createTraceability(Traceability traceability)")
                            .build());

                    logger.log("Trazabilidad creada", task, LogLevel.INFO, traceability, executionTime);
                })
                .then();
    }
}
