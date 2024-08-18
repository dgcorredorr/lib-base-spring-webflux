package com.fstech.provider.mapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * La interfaz base para todos los mappers en la aplicación. Los mappers se utilizan para
 * convertir objetos de dominio (DTO) en entidades y viceversa. Esta clase proporciona métodos
 * abstractos para realizar estas conversiones de manera eficiente y coherente en toda la aplicación.
 *
 * @param <E> El tipo de entidad que se mapea desde el modelo (DTO).
 * @param <M> El tipo de modelo (DTO) que se mapea desde la entidad.
 */
public interface ReactiveMapper<E, M> {

    /**
     * Convierte un objeto modelo (DTO) en una entidad.
     *
     * @param model El objeto modelo que se va a convertir en entidad.
     * @return La entidad resultante.
     */
    Mono<E> toEntity(M model);

    /**
     * Convierte una entidad en un objeto modelo (DTO).
     *
     * @param entity La entidad que se va a convertir en objeto modelo.
     * @return El objeto modelo resultante.
     */
    Mono<M> toModel(E entity);

    /**
     * Convierte un flujo de modelos (DTO) en un flujo de entidades.
     *
     * @param modelFlux Flujo de modelos que se va a convertir en entidades.
     * @return Flujo de entidades resultante.
     */
    default Flux<E> toEntityFlux(Flux<M> modelFlux) {
        return modelFlux.flatMap(this::toEntity);
    }

    /**
     * Convierte una flujo de entidades en un flujo de modelos (DTO).
     *
     * @param entityFlux Flujo de entidades que se va a convertir en modelos.
     * @return Flujo de modelos resultante.
     */
    default Flux<M> toModelFlux(Flux<E> entityFlux) {
        return entityFlux.flatMap(this::toModel);
    }
}
