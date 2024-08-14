package com.fstech.core;

import java.util.List;

import com.fstech.core.entity.Message;
import reactor.core.publisher.Mono;

/**
 * La interfaz MessageUseCase define operaciones para gestionar mensajes en la aplicación.
 */
public interface MessageUseCase {

    /**
     * Obtiene un mensaje por su identificador de manera reactiva.
     *
     * @param id El identificador del mensaje a buscar.
     * @return Un Mono que contiene el mensaje encontrado o vacío si no existe.
     */
    Mono<Message> getMessage(String id);

    /**
     * Obtiene la lista completa de mensajes disponibles de manera reactiva.
     *
     * @return Un List que contiene la lista de mensajes.
     */
    List<Message> getMessageList();
}
