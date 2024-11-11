package com.meli.application.service;

import java.util.List;

import com.meli.core.entity.Param;

import reactor.core.publisher.Mono;

/**
 * La interfaz ParamService define las operaciones disponibles para trabajar con parámetros en la aplicación.
 */
public interface ParamService {

    /**
     * Obtiene un parámetro específico según su identificador de manera reactiva.
     *
     * @param id El identificador único del parámetro.
     * @return Un Mono que contiene el parámetro correspondiente al ID proporcionado.
     */
    Mono<Param<?>> getParam(String id);

    /**
     * Mapea un parámetro utilizando un tipo específico de mapeo.
     *
     * @param paramMapping El tipo de mapeo de parámetro a aplicar.
     * @return El parámetro mapeado.
     */
    <T> T mapParam(String paramMapping, Class<T> type);

    <T> List<T> mapParamList(String string, Class<T> class1);
}
