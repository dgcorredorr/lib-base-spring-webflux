package com.fstech.provider;

import com.fstech.core.entity.ServiceError;

/**
 * La interfaz ServiceErrorProvider define métodos para registrar errores de servicio en la aplicación.
 * Proporciona una operación para crear un registro de error de servicio.
 */
public interface ServiceErrorProvider {

    /**
     * Crea un registro de error de servicio en el sistema de manera reactiva.
     *
     * @param serviceError El objeto ServiceError que contiene información sobre el error de servicio.
     */
    void createServiceError(ServiceError serviceError);
}
