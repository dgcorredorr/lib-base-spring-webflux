package com.meli.provider;

import com.meli.core.entity.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Proveedor de parámetros utilizado para interactuar con el almacenamiento de parámetros.
 */
public interface ParamProvider {

    /**
     * Obtiene todos los parámetros disponibles de manera reactiva.
     *
     * @return Un Flux que contiene la lista de parámetros.
     */
    Flux<Param<?>> getParams();

    /**
     * Obtiene un parámetro por su identificador de manera reactiva.
     *
     * @param id El identificador del parámetro a buscar.
     * @return Un Mono que contiene el parámetro encontrado o vacío si no existe.
     */
    Mono<Param<?>> getParam(String id);
}
