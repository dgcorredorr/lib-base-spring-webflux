package com.meli.provider.mapper;

import org.springframework.stereotype.Component;
import com.meli.core.entity.Param;
import com.meli.provider.model.ParamModel;
import reactor.core.publisher.Mono;

/**
 * El Mapper concreto utilizado para convertir objetos de tipo {@link Param} a {@link ParamModel} y viceversa.
 * Este Mapper se utiliza para facilitar la conversión entre las entidades y los modelos utilizados en la capa
 * de proveedores de la aplicación.
 *
 * @see Mapper
 * @see Param
 * @see ParamModel
 */
@Component
public class ParamMapper implements ReactiveMapper<Param<?>, ParamModel<?>> {

    /**
     * Convierte un objeto de tipo {@link ParamModel} en una entidad de tipo {@link Param}.
     *
     * @param model El objeto de tipo {@link ParamModel} que se va a convertir en entidad.
     * @return La entidad de tipo {@link Param} resultante.
     */
    @Override
    public Mono<Param<?>> toEntity(ParamModel<?> model) {
        return Mono.justOrEmpty(model)
                   .map(m -> Param.builder()
                                  .paramId(m.getParamId())
                                  .description(m.getDescription())
                                  .status(m.getStatus())
                                  .values(m.getValues())
                                  .build());
    }

    /**
     * Convierte una entidad de tipo {@link Param} en un objeto de tipo {@link ParamModel}.
     *
     * @param entity La entidad de tipo {@link Param} que se va a convertir en modelo.
     * @return El objeto de tipo {@link ParamModel} resultante.
     */
    @Override
    public Mono<ParamModel<?>> toModel(Param<?> entity) {
        return Mono.justOrEmpty(entity)
                   .map(e -> ParamModel.builder()
                                       .paramId(e.getParamId())
                                       .description(e.getDescription())
                                       .status(e.getStatus())
                                       .values(e.getValues())
                                       .build());
    }
}
