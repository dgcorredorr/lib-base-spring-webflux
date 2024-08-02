package com.fstech.provider.mapper;

import org.springframework.stereotype.Component;

import com.fstech.core.entity.ServiceError;
import com.fstech.provider.model.ServiceErrorModel;

/**
 * El Mapper concreto utilizado para convertir objetos de tipo {@link ServiceError} a {@link ServiceErrorModel} y viceversa.
 * Este Mapper se utiliza para facilitar la conversión entre las entidades y los modelos utilizados en la capa
 * de proveedores de la aplicación.
 *
 * @see Mapper
 * @see ServiceError
 * @see ServiceErrorModel
 */
@Component
public class ServiceErrorMapper implements Mapper<ServiceError, ServiceErrorModel> {

    @Override
    public ServiceError toEntity(ServiceErrorModel model) {
        throw new UnsupportedOperationException("toEntity not implemented");
    }

    /**
     * Convierte una entidad de tipo {@link ServiceError} en un objeto de tipo {@link ServiceErrorModel}.
     *
     * @param entity La entidad de tipo {@link ServiceError} que se va a convertir en objeto de tipo {@link ServiceErrorModel}.
     * @return El objeto de tipo {@link ServiceErrorModel} resultante.
     */
    @Override
    public ServiceErrorModel toModel(ServiceError entity) {
        return (entity != null) ? ServiceErrorModel.builder()
                .transactionId(entity.getTransactionId())
                .success(entity.getSuccess())
                .origin(entity.getOrigin())
                .method(entity.getMethod())
                .task(entity.getTask())
                .errorClass(entity.getErrorClass())
                .errorMethod(entity.getErrorMethod())
                .message(entity.getMessage())
                .stackTrace(entity.getStackTrace())
                .request(entity.getRequest())
                .response(entity.getResponse())
                .build() : null;
    }
}
