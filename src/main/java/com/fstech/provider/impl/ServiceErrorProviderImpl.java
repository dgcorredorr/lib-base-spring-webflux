package com.fstech.provider.impl;

import org.springframework.stereotype.Component;
import com.fstech.core.entity.ServiceError;
import com.fstech.provider.ServiceErrorProvider;
import com.fstech.provider.mapper.ServiceErrorMapper;
import com.fstech.provider.repository.ServiceErrorRepository;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Span;

/**
 * Implementación de la interfaz {@link ServiceErrorProvider} que proporciona
 * operaciones para registrar errores de servicio.
 *
 * <p>
 * Esta clase se encarga de interactuar con el repositorio de errores de
 * servicio y utiliza un mapeador para convertir
 * entre entidades del repositorio y objetos de dominio de errores de servicio.
 * También mide el tiempo de ejecución de
 * la operación y registra eventos relacionados con errores de servicio.
 * </p>
 *
 * @see ServiceErrorProvider
 */
@Component
public class ServiceErrorProviderImpl implements ServiceErrorProvider {

    private final ServiceErrorRepository serviceErrorRepository;
    private final ServiceErrorMapper serviceErrorMapper;

    public ServiceErrorProviderImpl(ServiceErrorRepository serviceErrorRepository,
            ServiceErrorMapper serviceErrorMapper) {
        this.serviceErrorRepository = serviceErrorRepository;
        this.serviceErrorMapper = serviceErrorMapper;
    }

    @Override
    public void createServiceError(ServiceError serviceError) {
        Span span = ElasticApm.currentSpan().startSpan("db", "mongodb", "save");
        span.setName("MongoDB Save Service Error");
        serviceErrorRepository.save(serviceErrorMapper.toModel(serviceError))
                .doOnError(Exception.class, span::captureException)
                .doFinally(signalType -> span.end())
                .subscribe();
    }
}
