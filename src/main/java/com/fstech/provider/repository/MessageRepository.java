package com.fstech.provider.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.fstech.provider.model.MessageModel;
import reactor.core.publisher.Mono;

/**
 * Repositorio de datos para la entidad MessageModel.
 * Proporciona métodos para acceder y administrar mensajes en la base de datos MongoDB de manera reactiva.
 */
@Repository
public interface MessageRepository extends ReactiveMongoRepository<MessageModel, String> {

    /**
     * Busca un mensaje por su identificador de manera reactiva.
     *
     * @param messageId El identificador del mensaje a buscar.
     * @return Un Mono que contiene el mensaje si se encuentra, o un Mono vacío si no se encuentra.
     */
    Mono<MessageModel> findByMessageId(String messageId);
}
