package com.fstech.provider;

import java.util.NoSuchElementException;

import com.fstech.core.entity.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * La interfaz MessageProvider define métodos para acceder y gestionar mensajes en la aplicación.
 * Proporciona operaciones para obtener mensajes, contar mensajes, actualizar mensajes y realizar consultas.
 */
public interface MessageProvider {

    /**
     * Obtiene una lista de mensajes con opciones de filtrado y ordenación de manera reactiva.
     *
     * @param sort       El campo por el cual ordenar los mensajes.
     * @param direction  La dirección de ordenación (ascendente o descendente).
     * @param filter     Filtros para limitar los resultados.
     * @return Un Flux de mensajes que cumplen con los criterios especificados.
     */
    Flux<Message> getMessages();

    /**
     * Obtiene un mensaje por su identificador único de manera reactiva.
     *
     * @param id El identificador único del mensaje.
     * @return Un Mono que contiene el mensaje correspondiente al ID proporcionado.
     */
    Mono<Message> getMessage(String id) throws NoSuchElementException;
}
