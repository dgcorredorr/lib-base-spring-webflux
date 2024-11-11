package com.meli.provider.repository;

import com.meli.provider.model.ParamModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repositorio para gestionar operaciones con ParamModel en una base de datos MongoDB de manera reactiva.
 *
 * @param <T> el tipo de los datos almacenados en ParamModel
 */
@Repository
public interface ParamRepository<T> extends ReactiveMongoRepository<ParamModel<T>, String> {
    
    /**
     * Encuentra una instancia de ParamModel con el paramId dado.
     *
     * @param paramId el ID del parámetro a buscar
     * @return un Mono que emite la instancia encontrada de ParamModel, o vacío si no se encuentra
     */
    Mono<ParamModel<T>> findByParamId(String paramId);
}
