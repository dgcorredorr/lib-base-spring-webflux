package com.meli.provider.mapper;

import org.springframework.stereotype.Component;

import com.meli.core.entity.Traceability;
import com.meli.provider.model.TraceabilityModel;

/**
 * El Mapper concreto utilizado para convertir objetos de tipo {@link Traceability} a {@link TraceabilityModel} y viceversa.
 * Este Mapper se utiliza para facilitar la conversión entre las entidades y los modelos utilizados en la capa
 * de proveedores de la aplicación.
 *
 * @see Mapper
 * @see Traceability
 * @see TraceabilityModel
 */
@Component
public class TraceabilityMapper implements Mapper<Traceability, TraceabilityModel> {

    @Override
    public Traceability toEntity(TraceabilityModel model) {
        throw new UnsupportedOperationException("toEntity not implemented");
    }

    /**
     * Convierte una entidad de tipo {@link Traceability} en un objeto de tipo {@link TraceabilityModel}.
     *
     * @param entity La entidad de tipo {@link Traceability} que se va a convertir en objeto de tipo {@link TraceabilityModel}.
     * @return El objeto de tipo {@link TraceabilityModel} resultante.
     */
    @Override
    public TraceabilityModel toModel(Traceability entity) {
        return (entity != null) ? TraceabilityModel.builder()
                .transactionId(entity.getTransactionId())
                .status(entity.getStatus())
                .origin(entity.getOrigin())
                .method(entity.getMethod())
                .task(entity.getTask())
                .request(entity.getRequest())
                .response(entity.getResponse())
                .durationMillis(entity.getDurationMillis())
                .build() : null;
    }
}
