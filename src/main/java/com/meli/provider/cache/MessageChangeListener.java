package com.meli.provider.cache;

import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

import com.meli.common.utils.enums.LogLevel;
import com.meli.common.utils.log.ServiceLogger;
import com.meli.common.utils.tasks.TaskService;
import com.meli.core.MessageUseCase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MessageChangeListener {

    private final ServiceLogger<MessageChangeListener> logger = new ServiceLogger<>(MessageChangeListener.class);
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final TaskService taskService;
    private final MessageUseCase messageUseCase;

    private static final String MESSAGE_CACHE_UPDATED = "MESSAGE_CACHE_UPDATED";

    public MessageChangeListener(ReactiveMongoTemplate reactiveMongoTemplate, MessageUseCase messageUseCase,
            TaskService taskService) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.messageUseCase = messageUseCase;
        this.taskService = taskService;
    }

    @PostConstruct
    public void init() {
        // Observa los cambios en la colección de forma reactiva
        Flux<ChangeStreamDocument<Document>> changeStream = reactiveMongoTemplate
                .getCollection("coll_message")
                .flatMapMany(collection -> collection.watch(Document.class));

        // Procesa cada cambio y recarga los mensajes en `messageUseCase`
        changeStream
                .filter(this::isRelevantChange)
                .flatMap(change -> messageUseCase.loadMessages()
                        .doOnSuccess(aVoid -> taskService.getTaskById(MESSAGE_CACHE_UPDATED)
                                .ifPresent(task -> logger.log(
                                        "Message cache updated",
                                        task,
                                        LogLevel.INFO,
                                        null,
                                        null))) // Log después de actualizar
                        .onErrorResume(e -> { // Maneja errores y continúa escuchando cambios
                            taskService.getTaskById(MESSAGE_CACHE_UPDATED)
                                    .ifPresent(task -> logger.log(
                                            "Error al cargar mensajes en caché",
                                            task,
                                            LogLevel.ERROR,
                                            null,
                                            null));
                            return Mono.empty();
                        }))
                .subscribe();
    }

    // Filtra solo los cambios relevantes (INSERT, UPDATE, REPLACE, DELETE)
    private boolean isRelevantChange(ChangeStreamDocument<Document> changeStreamDocument) {
        OperationType operationType = changeStreamDocument.getOperationType();
        return operationType == OperationType.INSERT ||
                operationType == OperationType.UPDATE ||
                operationType == OperationType.REPLACE ||
                operationType == OperationType.DELETE;
    }
}
