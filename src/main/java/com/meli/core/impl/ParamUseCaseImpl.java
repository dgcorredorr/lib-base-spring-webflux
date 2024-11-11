package com.meli.core.impl;

import org.springframework.stereotype.Service;

import com.meli.core.ParamUseCase;
import com.meli.core.entity.Param;
import com.meli.provider.ParamProvider;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementación de la interfaz {@link ParamUseCase} que proporciona la lógica
 * para el manejo de parámetros en la aplicación.
 *
 * <p>Esta clase se encarga de interactuar con un proveedor de parámetros para obtener
 * y gestionar parámetros dentro de la aplicación.</p>
 */
@Service
public class ParamUseCaseImpl implements ParamUseCase {

    private List<Param<?>> paramList = new CopyOnWriteArrayList<>();

    private final ParamProvider paramProvider;

    /**
     * Constructor de la clase que recibe una instancia de {@link ParamProvider}
     * para la obtención de parámetros y carga inicial de la lista de parámetros.
     *
     * @param paramProvider El proveedor de parámetros utilizado para interactuar con
     *                      el almacenamiento de parámetros.
     */
    public ParamUseCaseImpl(ParamProvider paramProvider) {
        this.paramProvider = paramProvider;
        this.loadParams().subscribe();
    }

    /**
     * Carga inicial de parámetros desde el proveedor de parámetros y los almacena
     * en la lista de parámetros.
     */
    @Override
    public Mono<Void> loadParams() {
        return this.paramProvider.getParams()
                   .collectList()
                   .doOnNext(params -> {
                        this.paramList.clear();
                        this.paramList.addAll(params);
                    })
                   .then();
    }

    @Override
    public Mono<Param<?>> getParam(String id) {
        return this.paramProvider.getParam(id);
    }

    @Override
    public List<Param<?>> getParamList() {
        return this.paramList;
    }
}
