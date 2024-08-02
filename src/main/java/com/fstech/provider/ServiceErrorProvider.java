package com.fstech.provider;

import com.fstech.core.entity.ServiceError;
import reactor.core.publisher.Mono;

/**
 * La interfaz ServiceErrorProvider define métodos para registrar errores de servicio en la aplicación.
 * Proporciona una operación para crear un registro de error de servicio.
 */
public interface ServiceErrorProvider {

    /**
     * Crea un registro de error de servicio en el sistema de manera reactiva.
     *
     * @param serviceError El objeto ServiceError que contiene información sobre el error de servicio.
     * @return Un Mono<Void> que indica la finalización de la operación.
     */
    Mono<Void> createServiceError(ServiceError serviceError);
}
