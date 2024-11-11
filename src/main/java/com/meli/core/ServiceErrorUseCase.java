package com.meli.core;

import com.meli.core.entity.ServiceError;

/**
 * La interfaz ServiceErrorUseCase define operaciones para gestionar errores de servicio en la aplicación.
 */
public interface ServiceErrorUseCase {

    /**
     * Crea un registro de error de servicio de manera reactiva.
     *
     * @param serviceError El objeto ServiceError que contiene la información del error de servicio.
     */
    void createServiceError(ServiceError serviceError);
}
