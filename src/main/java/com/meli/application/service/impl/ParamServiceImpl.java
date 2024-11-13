package com.meli.application.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import com.meli.application.service.ParamService;
import com.meli.core.ParamUseCase;
import com.meli.core.entity.Param;
import reactor.core.publisher.Mono;

/**
 * Implementación del servicio para operaciones relacionadas con parámetros.
 *
 * <p>
 * Esta clase proporciona una implementación concreta de {@link ParamService},
 * delegando las operaciones relacionadas con parámetros al caso de uso
 * {@link ParamUseCase}.
 * Se encarga de la lógica de servicio, como la obtención y mapeo de parámetros.
 * </p>
 *
 * @see ParamService
 * @see ParamUseCase
 */
@Service
public class ParamServiceImpl implements ParamService {

    private final ParamUseCase paramUseCase;

    /**
     * Constructor para inyectar dependencias del caso de uso de parámetros.
     *
     * @param paramUseCase El caso de uso de parámetros que maneja la lógica de
     *                     negocio.
     */
    public ParamServiceImpl(ParamUseCase paramUseCase) {
        this.paramUseCase = paramUseCase;
    }

    @Override
    public Mono<Param<?>> getParam(String id) {
        return this.paramUseCase.getParam(id);
    }

    /**
     * Mapea un tipo de mapeo de parámetro a su parámetro correspondiente.
     *
     * <p>
     * Este método busca un parámetro correspondiente al código de mapeo
     * proporcionado
     * y devuelve su contenido. Si no se encuentra el parámetro, devuelve un mensaje
     * por defecto
     * indicando que el parámetro no se encontró.
     * </p>
     *
     * @param paramMapping El código de mapeo de parámetro a mapear.
     * @return El parámetro correspondiente al código proporcionado.
     */
    @Override
    public <T> T mapParam(String paramMapping, Class<T> type) {
        return this.paramUseCase
                .getParamList()
                .stream()
                .filter(param -> param.getParamId().equals(paramMapping))
                .findFirst()
                .map(Param::getValues)
                .map(type::cast)
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> mapParamList(String string, Class<T> class1) {
        return this.paramUseCase
            .getParamList()
            .stream()
            .filter(param -> param.getParamId().equals(string))
            .findFirst()
            .map(Param::getValues)
            .map(values -> (List<T>) values)
            .orElse(null);
    }
}
