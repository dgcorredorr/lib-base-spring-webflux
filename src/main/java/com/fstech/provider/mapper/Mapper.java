package com.fstech.provider.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * La interfaz base para todos los mappers en la aplicación. Los mappers se utilizan para
 * convertir objetos de dominio (DTO) en entidades y viceversa. Esta clase proporciona métodos
 * abstractos para realizar estas conversiones de manera eficiente y coherente en toda la aplicación.
 *
 * @param <E> El tipo de entidad que se mapea desde el modelo (DTO).
 * @param <M> El tipo de modelo (DTO) que se mapea desde la entidad.
 */
public interface Mapper<E, M> {

    /**
     * Convierte un objeto modelo (DTO) en una entidad.
     *
     * @param model El objeto modelo que se va a convertir en entidad.
     * @return La entidad resultante.
     */
    E toEntity(M model);

    /**
     * Convierte una entidad en un objeto modelo (DTO).
     *
     * @param entity La entidad que se va a convertir en objeto modelo.
     * @return El objeto modelo resultante.
     */
    M toModel(E entity);

    /**
     * Convierte una lista de modelos (DTO) en una lista de entidades.
     *
     * @param modelList La lista de modelos que se va a convertir en entidades.
     * @return La lista de entidades resultante.
     */
    default List<E> toEntityList(List<M> modelList) {
        return (modelList == null) ? null : modelList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
