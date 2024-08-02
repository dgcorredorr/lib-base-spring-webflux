package com.fstech.application.service;

import com.fstech.core.entity.ServiceError;
import reactor.core.publisher.Mono;

/**
 * La interfaz ServiceErrorService define una operación para crear un registro de error de servicio.
 */
public interface ServiceErrorService {

    /**
     * Crea un registro de error de servicio de manera reactiva.
     *
     * @param serviceError El objeto ServiceError que contiene la información del error.
     * @return Un Mono<Void> que indica la finalización de la operación.
     */
    Mono<Void> createServiceError(ServiceError serviceError);
}
