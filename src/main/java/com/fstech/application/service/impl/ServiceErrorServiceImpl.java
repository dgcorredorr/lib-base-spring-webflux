package com.fstech.application.service.impl;

import org.springframework.stereotype.Service;

import com.fstech.application.service.ServiceErrorService;
import com.fstech.core.ServiceErrorUseCase;
import com.fstech.core.entity.ServiceError;
import reactor.core.publisher.Mono;

/**
 * Implementación del servicio para operaciones relacionadas con errores de servicio.
 *
 * <p>Esta clase proporciona una implementación concreta de {@link ServiceErrorService},
 * delegando las operaciones relacionadas con el manejo de errores de servicio al caso de uso
 * {@link ServiceErrorUseCase}. Se encarga de la lógica de servicio, como la creación de
 * registros de errores de servicio.</p>
 *
 * @see ServiceErrorService
 * @see ServiceErrorUseCase
 */
@Service
public class ServiceErrorServiceImpl implements ServiceErrorService {
    private final ServiceErrorUseCase serviceErrorUseCase;

    /**
     * Constructor para inyectar dependencias del caso de uso de errores de servicio.
     *
     * @param serviceErrorUseCase El caso de uso de errores de servicio que maneja la lógica de negocio.
     */
    public ServiceErrorServiceImpl(ServiceErrorUseCase serviceErrorUseCase) {
        this.serviceErrorUseCase = serviceErrorUseCase;
    }

    /**
     * Crea un registro de error de servicio de manera reactiva.
     *
     * <p>Este método invoca el caso de uso {@link ServiceErrorUseCase} para crear un
     * registro de error de servicio en el sistema. Es útil para mantener un seguimiento
     * de los errores que ocurren durante la ejecución de la aplicación y para facilitar
     * la depuración y el análisis de problemas.</p>
     *
     * @param serviceError El objeto {@link ServiceError} que contiene la información del error a registrar.
     * @return Un Mono<Void> que indica la finalización de la operación.
     */
    @Override
    public Mono<Void> createServiceError(ServiceError serviceError) {
        return this.serviceErrorUseCase.createServiceError(serviceError);
    }
}
