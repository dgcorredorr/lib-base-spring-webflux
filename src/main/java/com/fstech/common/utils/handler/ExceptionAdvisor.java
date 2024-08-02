package com.fstech.common.utils.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import com.fstech.application.dto.GenericResponse;
import com.fstech.application.service.MessageService;
import com.fstech.application.service.ServiceErrorService;
import com.fstech.application.service.TraceabilityService;
import com.fstech.common.utils.ServiceLogger;
import com.fstech.common.utils.enums.LogLevel;
import com.fstech.common.utils.enums.MessageMapping;
import com.fstech.common.utils.enums.Task;
import com.fstech.common.utils.enums.TraceabilityStatus;
import com.fstech.common.utils.enums.TraceabilityTask;
import com.fstech.common.utils.exception.ServiceException;
import com.fstech.core.entity.ServiceError;
import com.fstech.core.entity.Traceability;

import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * Controlador de excepciones global que maneja diversas excepciones lanzadas
 * durante la ejecución de la aplicación y las convierte en respuestas HTTP.
 *
 * <p>Este advisor de excepciones captura excepciones específicas y las mapea
 * a respuestas HTTP, proporcionando un manejo uniforme y centralizado de errores.</p>
 *
 * @see ControllerAdvice
 */
@ControllerAdvice
public class ExceptionAdvisor {

    private final ServiceLogger<ExceptionAdvisor> logger;
    private final TraceabilityService traceabilityService;
    private final ServiceErrorService serviceErrorService;
    private final MessageService messageService;

    public ExceptionAdvisor(ServiceLogger<ExceptionAdvisor> logger, TraceabilityService traceabilityService, ServiceErrorService serviceErrorService,
                            MessageService messageService) {
        logger.setLoggerClass(ExceptionAdvisor.class);
        this.logger = logger;
        this.traceabilityService = traceabilityService;
        this.serviceErrorService = serviceErrorService;
        this.messageService = messageService;
    }

    @ExceptionHandler(ServiceException.class)
    public Mono<ResponseEntity<GenericResponse>> handleServiceException(ServiceException ex, ServerWebExchange exchange) {
        return handleExceptionInternal(ex, exchange, HttpStatus.CONFLICT, TraceabilityStatus.FAILED, LogLevel.WARNING, ex.getTask());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<GenericResponse>> handleGenericException(Exception ex, ServerWebExchange exchange) {
        return handleExceptionInternal(ex, exchange, HttpStatus.INTERNAL_SERVER_ERROR, TraceabilityStatus.ERROR, LogLevel.ERROR, Task.EXCEPTION_MANAGER);
    }

    private Mono<ResponseEntity<GenericResponse>> handleExceptionInternal(Exception ex, ServerWebExchange exchange, HttpStatus status,
                                                                          TraceabilityStatus traceabilityStatus, LogLevel logLevel, Task task) {

        exchange.getAttributes().put("TRACEABILITY_STATUS", traceabilityStatus);

        String transactionId = exchange.getLogPrefix();

        return createTraceability(transactionId, exchange, traceabilityStatus, task)
                .then(createServiceError(ex, exchange, transactionId, task))
                .then(logException(exchange, ex, logLevel))
                .then(createErrorResponse(exchange, status));
    }

    private Mono<Void> createTraceability(String transactionId, ServerWebExchange exchange, TraceabilityStatus traceabilityStatus, Task task) {
        return traceabilityService.createTraceability(Traceability.builder()
                .transactionId(transactionId)
                .task(TraceabilityTask.REQUEST_ERROR)
                .method(exchange.getRequest().getMethod())
                .status(traceabilityStatus)
                .origin(exchange.getRequest().getURI().toString())
                .request(exchange.getRequest())
                .response(exchange.getResponse())
                .build())
                .then();
    }

    private Mono<Void> createServiceError(Exception ex, ServerWebExchange exchange, String transactionId, Task task) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        return serviceErrorService.createServiceError(ServiceError.builder()
                .transactionId(transactionId)
                .success(false)
                .origin(exchange.getRequest().getURI().toString())
                .method(exchange.getRequest().getMethod())
                .task(task)
                .errorClass(ex instanceof ServiceException ? ((ServiceException) ex).getTask().getOrigin().getOriginClass() : "")
                .errorMethod(ex instanceof ServiceException ? ((ServiceException) ex).getTask().getOrigin().getOriginMethod() : "")
                .message(ex.getMessage())
                .stackTrace(Arrays.toString(stackTrace))
                .request(exchange.getRequest())
                .response(exchange.getResponse())
                .build())
                .then();
    }

    private Mono<Void> logException(ServerWebExchange exchange, Exception ex, LogLevel logLevel) {
        return Mono.fromRunnable(() -> logger.log("Excepción - " + exchange.getRequest().getURI(),
                Task.EXCEPTION_MANAGER,
                logLevel,
                ex,
                null));
    }

    private Mono<ResponseEntity<GenericResponse>> createErrorResponse(ServerWebExchange exchange, HttpStatus status) {
        return messageService.mapMessage(MessageMapping.DEFAULT_ERROR)
                .map(message -> GenericResponse.builder()
                        .message(message)
                        .origin(exchange.getRequest().getURI().toString())
                        .success(false)
                        .build())
                .map(body -> new ResponseEntity<>(body, status));
    }
}
