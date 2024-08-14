package com.fstech.application.service;

import com.fstech.common.utils.enums.MessageMapping;
import com.fstech.core.entity.Message;
import reactor.core.publisher.Mono;

/**
 * La interfaz MessageService define las operaciones disponibles para trabajar con mensajes en la aplicación.
 */
public interface MessageService {

    /**
     * Obtiene un mensaje específico según su identificador de manera reactiva.
     *
     * @param id El identificador único del mensaje.
     * @return Un Mono que contiene el mensaje correspondiente al ID proporcionado.
     */
    Mono<Message> getMessage(String id);

    /**
     * Mapea un mensaje utilizando un MessageMapping específico.
     *
     * @param messageMapping El tipo de mapeo de mensaje a aplicar.
     * @return El mensaje mapeado.
     */
    String mapMessage(MessageMapping messageMapping);
}
