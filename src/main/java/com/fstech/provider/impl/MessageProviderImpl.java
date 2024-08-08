package com.fstech.provider.impl;

import org.springframework.stereotype.Component;

import com.fstech.common.exception.ServiceException;
import com.fstech.common.utils.enums.Task;
import com.fstech.core.entity.Message;
import com.fstech.provider.MessageProvider;
import com.fstech.provider.mapper.MessageMapper;
import com.fstech.provider.repository.MessageRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.NoSuchElementException;

/**
 * Implementación de la interfaz {@link MessageProvider} que proporciona operaciones para acceder a mensajes en el sistema.
 *
 * <p>Esta clase se encarga de interactuar con el repositorio de mensajes y utiliza un mapeador para convertir
 * entre entidades del repositorio y objetos de dominio de mensajes.</p>
 *
 * @see MessageProvider
 */
@Component
public class MessageProviderImpl implements MessageProvider {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageProviderImpl(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public Flux<Message> getMessages() {
        return this.messageRepository.findAll()
                                   .flatMap(messageMapper::toEntity);
    }

    @Override
    public Mono<Message> getMessage(String id) {
        Task task = Task.GET_MESSAGE;
                                       task.setOrigin(Task.Origin.builder()
                                               .originClass("MessageUseCaseImpl")
                                               .originMethod("getMessage(String id)")
                                               .build());
        return this.messageRepository.findByMessageId(id)
                                     .switchIfEmpty(Mono.error(new ServiceException("Mensaje no encontrado", null, task, NoSuchElementException.class, null)
                                     ))
                                     .flatMap(messageMapper::toEntity);
    }
}
