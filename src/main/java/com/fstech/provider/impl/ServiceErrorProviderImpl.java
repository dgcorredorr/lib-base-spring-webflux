package com.fstech.provider.impl;

import org.springframework.stereotype.Component;

import com.fstech.common.utils.enums.LogLevel;
import com.fstech.common.utils.enums.Task;
import com.fstech.common.utils.log.ServiceLogger;
import com.fstech.core.entity.ServiceError;
import com.fstech.provider.ServiceErrorProvider;
import com.fstech.provider.mapper.ServiceErrorMapper;
import com.fstech.provider.repository.ServiceErrorRepository;

import reactor.core.publisher.Mono;

/**
 * Implementación de la interfaz {@link ServiceErrorProvider} que proporciona operaciones para registrar errores de servicio.
 *
 * <p>Esta clase se encarga de interactuar con el repositorio de errores de servicio y utiliza un mapeador para convertir
 * entre entidades del repositorio y objetos de dominio de errores de servicio. También mide el tiempo de ejecución de
 * la operación y registra eventos relacionados con errores de servicio.</p>
 *
 * @see ServiceErrorProvider
 */
@Component
public class ServiceErrorProviderImpl implements ServiceErrorProvider {

    private final ServiceLogger<ServiceErrorProviderImpl> logger = new ServiceLogger<>(ServiceErrorProviderImpl.class);
    private final ServiceErrorRepository serviceErrorRepository;
    private final ServiceErrorMapper serviceErrorMapper;

    public ServiceErrorProviderImpl(ServiceErrorRepository serviceErrorRepository, ServiceErrorMapper serviceErrorMapper) {
        this.serviceErrorRepository = serviceErrorRepository;
        this.serviceErrorMapper = serviceErrorMapper;
    }

    @Override
    public Mono<Void> createServiceError(ServiceError serviceError) {
        long startTime = System.currentTimeMillis();

        return serviceErrorRepository.save(serviceErrorMapper.toModel(serviceError))
                .doOnSuccess(savedServiceError -> {
                    long endTime = System.currentTimeMillis();
                    long executionTime = endTime - startTime;

                    Task task = Task.CREATE_SERVICE_ERROR;
                    task.setOrigin(Task.Origin.builder()
                            .originClass("ServiceErrorProviderImpl")
                            .originMethod("createServiceError(ServiceError serviceError)")
                            .build());

                    logger.log("Error de servicio creado", task, LogLevel.INFO, serviceError, executionTime);
                })
                .then();
    }
}
