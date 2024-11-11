package com.meli.core;

import com.meli.core.entity.Param;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 * Interfaz que define los casos de uso para el manejo de parámetros en la aplicación.
 */
public interface ParamUseCase {

    /**
     * Obtiene un parámetro por su identificador de manera reactiva.
     *
     * @param id El identificador del parámetro a buscar.
     * @return Un Mono que contiene el parámetro encontrado o vacío si no existe.
     */
    Mono<Param<?>> getParam(String id);

    /**
     * Obtiene la lista completa de parámetros disponibles de manera reactiva.
     *
     * @return Un List que contiene la lista de parámetros.
     */
    List<Param<?>> getParamList();

    /**
     * Carga una lista de parámetros de manera reactiva desde el proveedor de parámetros
     * y los almacena en la lista de parámetros.
     *
     * @return Un Mono que indica la finalización de la operación.
     */
    Mono<Void> loadParams();
}
