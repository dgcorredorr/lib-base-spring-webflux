package com.meli.provider.impl;

import org.springframework.stereotype.Component;

import com.meli.common.exception.ServiceException;
import com.meli.common.utils.tasks.Task;
import com.meli.common.utils.tasks.TaskService;
import com.meli.core.entity.Message;
import com.meli.provider.MessageProvider;
import com.meli.provider.mapper.MessageMapper;
import com.meli.provider.repository.MessageRepository;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Span;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.NoSuchElementException;

/**
 * Implementación de la interfaz {@link MessageProvider} que proporciona
 * operaciones para acceder a mensajes en el sistema.
 *
 * <p>
 * Esta clase se encarga de interactuar con el repositorio de mensajes y utiliza
 * un mapeador para convertir
 * entre entidades del repositorio y objetos de dominio de mensajes.
 * </p>
 *
 * @see MessageProvider
 */
@Component
public class MessageProviderImpl implements MessageProvider {

        private final MessageRepository messageRepository;
        private final MessageMapper messageMapper;
        private final TaskService taskService;

        public MessageProviderImpl(MessageRepository messageRepository, MessageMapper messageMapper,
                        TaskService taskService) {
                this.messageRepository = messageRepository;
                this.messageMapper = messageMapper;
                this.taskService = taskService;
        }

        @Override
        public Flux<Message> getMessages() {
                Span span = ElasticApm.currentSpan().startSpan("db", "mongodb", "query");
                try {
                        span.setName("MongoDB Find All Messages");
                        return this.messageMapper.toEntityFlux(this.messageRepository.findAll());
                } catch (Exception e) {
                        span.captureException(e);
                        throw e;
                } finally {
                        span.end();
                }

        }

        @Override
        public Mono<Message> getMessage(String id) {
                Span span = ElasticApm.currentSpan().startSpan("db", "mongodb", "query");
                span.setName("MongoDB Find Message by ID");
                Task task = taskService.getTaskById("GET_MESSAGE").get();
                task.setOrigin(Task.Origin.builder()
                                .originClass("MessageUseCaseImpl")
                                .originMethod("getMessage(String id)")
                                .build());
                return this.messageRepository.findByMessageId(id)
                                .switchIfEmpty(Mono.error(
                                                new ServiceException("Mensaje no encontrado", null, task,
                                                                NoSuchElementException.class, null)))
                                .flatMap(messageMapper::toEntity)
                                .doOnError(Exception.class, span::captureException)
                                .doFinally(signalType -> span.end());
        }
}
