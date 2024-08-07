package com.fstech.core.impl;

import org.springframework.stereotype.Service;

import com.fstech.core.ServiceErrorUseCase;
import com.fstech.core.entity.ServiceError;
import com.fstech.provider.ServiceErrorProvider;

/**
 * Implementación de la interfaz {@link ServiceErrorUseCase} que proporciona la lógica
 * para la gestión de errores de servicio en la aplicación.
 *
 * <p>Esta clase se encarga de interactuar con un proveedor de errores de servicio para
 * crear y registrar errores de servicio en la aplicación.</p>
 */
@Service
public class ServiceErrorUseCaseImpl implements ServiceErrorUseCase {
    private final ServiceErrorProvider serviceErrorProvider;

    /**
     * Constructor de la clase que recibe una instancia de {@link ServiceErrorProvider}
     * para la gestión de errores de servicio.
     *
     * @param serviceErrorProvider El proveedor de errores de servicio utilizado para
     *                             registrar errores de servicio en la aplicación.
     */
    public ServiceErrorUseCaseImpl(ServiceErrorProvider serviceErrorProvider) {
        this.serviceErrorProvider = serviceErrorProvider;
    }

    @Override
    public void createServiceError(ServiceError serviceError) {
        this.serviceErrorProvider.createServiceError(serviceError);
    }
}
