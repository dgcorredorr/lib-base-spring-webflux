package com.meli.provider.cache;

import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

import com.meli.common.utils.enums.LogLevel;
import com.meli.common.utils.log.ServiceLogger;
import com.meli.common.utils.tasks.TaskService;
import com.meli.core.ParamUseCase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ParamChangeListener {

    private final ServiceLogger<ParamChangeListener> logger = new ServiceLogger<>(ParamChangeListener.class);
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final TaskService taskService;
    private final ParamUseCase paramUseCase;

    private static final String PARAM_CACHE_UPDATED = "PARAM_CACHE_UPDATED";

    public ParamChangeListener(ReactiveMongoTemplate reactiveMongoTemplate, ParamUseCase paramUseCase,
            TaskService taskService) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.paramUseCase = paramUseCase;
        this.taskService = taskService;
    }

    @PostConstruct
    public void init() {
        // Observa los cambios en la colección de forma reactiva
        Flux<ChangeStreamDocument<Document>> changeStream = reactiveMongoTemplate
                .getCollection("coll_param")
                .flatMapMany(collection -> collection.watch(Document.class));

        // Procesa cada cambio y recarga los parámetros en `paramUseCase`
        changeStream
                .filter(this::isRelevantChange)
                .flatMap(change -> paramUseCase.loadParams()
                        .doOnSuccess(aVoid -> taskService.getTaskById(PARAM_CACHE_UPDATED)
                                .ifPresent(task -> logger.log(
                                        "Param cache updated",
                                        task,
                                        LogLevel.INFO,
                                        null,
                                        null))) // Log después de actualizar
                        .onErrorResume(e -> { // Maneja errores y continúa escuchando cambios
                            taskService.getTaskById(PARAM_CACHE_UPDATED)
                                    .ifPresent(task -> logger.log(
                                            "Error al cargar parámetros en caché",
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
