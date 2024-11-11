package com.meli.provider.impl;

import com.meli.core.entity.Param;
import com.meli.provider.ParamProvider;
import com.meli.provider.mapper.ParamMapper;
import com.meli.provider.model.ParamModel;
import com.meli.provider.repository.ParamRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementación del proveedor de parámetros que interactúa con la base de datos MongoDB.
 */
@Service
public class ParamProviderImpl implements ParamProvider {

    private final ParamRepository<ParamModel<?>> paramRepository;
    private final ParamMapper paramMapper;

    public ParamProviderImpl(ParamRepository<ParamModel<?>> paramRepository, ParamMapper paramMapper) {
        this.paramRepository = paramRepository;
        this.paramMapper = paramMapper;
    }

    @Override
    public Flux<Param<?>> getParams() {
        return paramRepository.findAll()
                .flatMap(paramMapper::toEntity);
    }

    @Override
    public Mono<Param<?>> getParam(String id) {
        return paramRepository.findById(id)
                .flatMap(paramMapper::toEntity);
    }
}
