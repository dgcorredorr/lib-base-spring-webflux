package com.fstech.common.utils.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

import com.fstech.application.dto.GenericResponse;
import com.fstech.application.service.MessageService;
import com.fstech.application.service.ServiceErrorService;
import com.fstech.application.service.TraceabilityService;
import com.fstech.common.utils.enums.LogLevel;
import com.fstech.common.utils.enums.MessageMapping;
import com.fstech.common.utils.enums.Task;
import com.fstech.common.utils.enums.TraceabilityStatus;
import com.fstech.common.utils.enums.TraceabilityTask;
import com.fstech.common.utils.exception.ServiceException;
import com.fstech.common.utils.log.ServiceLogger;
import com.fstech.core.entity.ServiceError;
import com.fstech.core.entity.Traceability;

import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador de excepciones global que maneja diversas excepciones lanzadas
 * durante la ejecución de la aplicación y las convierte en respuestas HTTP.
 *
 * <p>
 * Este advisor de excepciones captura excepciones específicas y las mapea
 * a respuestas HTTP, proporcionando un manejo uniforme y centralizado de
 * errores.
 * </p>
 *
 * @see ControllerAdvice
 */
@ControllerAdvice
public class ExceptionAdvisor {

    private final ServiceLogger<ExceptionAdvisor> logger = new ServiceLogger<>(ExceptionAdvisor.class);
    private final TraceabilityService traceabilityService;
    private final ServiceErrorService serviceErrorService;
    private final MessageService messageService;

    public ExceptionAdvisor(TraceabilityService traceabilityService,
            ServiceErrorService serviceErrorService, MessageService messageService) {
        this.traceabilityService = traceabilityService;
        this.serviceErrorService = serviceErrorService;
        this.messageService = messageService;
    }

    @ExceptionHandler(ServiceException.class)
    public Mono<ResponseEntity<GenericResponse>> handleServiceException(ServiceException ex,
            ServerWebExchange exchange) {
        HttpStatus status = HttpStatus.CONFLICT;
        TraceabilityStatus traceabilityStatus = TraceabilityStatus.FAILED;
        LogLevel logLevel = LogLevel.WARN;
        return handleExceptionInternal(ex, exchange, status, traceabilityStatus, logLevel, ex.getTask(),
                ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<GenericResponse>> handleValidationException(MethodArgumentNotValidException ex,
            ServerWebExchange exchange) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        TraceabilityStatus traceabilityStatus = TraceabilityStatus.ERROR;
        LogLevel logLevel = LogLevel.ERROR;
        return handleExceptionInternal(new Exception(errorMessage), exchange, status, traceabilityStatus, logLevel,
                Task.EXCEPTION_MANAGER, errorMessage);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<GenericResponse>> handleServerWebInputException(ServerWebInputException ex,
            ServerWebExchange exchange) {
        String errorMessage = "Missing or invalid parameter: " + ex.getReason();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        TraceabilityStatus traceabilityStatus = TraceabilityStatus.ERROR;
        LogLevel logLevel = LogLevel.ERROR;
        return handleExceptionInternal(new Exception(errorMessage), exchange, status, traceabilityStatus, logLevel,
                Task.EXCEPTION_MANAGER, errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<GenericResponse>> handleGenericException(Exception ex, ServerWebExchange exchange) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        TraceabilityStatus traceabilityStatus = TraceabilityStatus.ERROR;
        LogLevel logLevel = LogLevel.ERROR;
        return handleExceptionInternal(ex, exchange, status, traceabilityStatus, logLevel, Task.EXCEPTION_MANAGER,
                null);
    }

    private Mono<ResponseEntity<GenericResponse>> handleExceptionInternal(Exception ex, ServerWebExchange exchange,
            HttpStatus status,
            TraceabilityStatus traceabilityStatus, LogLevel logLevel, Task task, String exceptionMessage) {

        exchange.getAttributes().put("TRACEABILITY_STATUS", traceabilityStatus);

        String transactionId = exchange.getLogPrefix();
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("exception", ex.getClass().getName());
        errorDetails.put("message", ex.getMessage());

        return createTraceability(transactionId, exchange, traceabilityStatus)
                .then(createServiceError(ex, exchange, transactionId, task))
                .then(logException(exchange, ex, logLevel))
                .then(createErrorResponse(exchange, status, transactionId, exceptionMessage, errorDetails));
    }

    private Mono<Void> createTraceability(String transactionId, ServerWebExchange exchange,
            TraceabilityStatus traceabilityStatus) {
        return traceabilityService.createTraceability(Traceability.builder()
                .transactionId(transactionId)
                .task(TraceabilityTask.REQUEST_ERROR)
                .method(exchange.getRequest().getMethod())
                .status(traceabilityStatus)
                .origin(exchange.getRequest().getURI().getPath())
                .request(exchange.getRequest().toString())
                .response(exchange.getResponse().toString())
                .build())
                .then();
    }

    private Mono<Void> createServiceError(Exception ex, ServerWebExchange exchange, String transactionId, Task task) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        return serviceErrorService.createServiceError(ServiceError.builder()
                .transactionId(transactionId)
                .success(false)
                .origin(exchange.getRequest().getURI().getPath())
                .method(exchange.getRequest().getMethod())
                .task(task)
                .errorClass(
                        ex instanceof ServiceException ? ((ServiceException) ex).getTask().getOrigin().getOriginClass()
                                : "")
                .errorMethod(
                        ex instanceof ServiceException ? ((ServiceException) ex).getTask().getOrigin().getOriginMethod()
                                : "")
                .message(ex.getMessage())
                .stackTrace(Arrays.toString(stackTrace))
                .request(exchange.getRequest().toString())
                .response(exchange.getResponse().toString())
                .build())
                .then();
    }

    private Mono<Void> logException(ServerWebExchange exchange, Exception ex, LogLevel logLevel) {
        return Mono.fromRunnable(() -> logger.log("Excepción - " + exchange.getRequest().getURI().getPath(),
                Task.EXCEPTION_MANAGER, logLevel, ex, null));
    }

    private Mono<ResponseEntity<GenericResponse>> createErrorResponse(ServerWebExchange exchange, HttpStatus status,
            String transactionId, String exceptionMessage, Map<String, Object> errorDetails) {
        return messageService.mapMessage(MessageMapping.DEFAULT_ERROR)
                .map(mappedMessage -> {
                    GenericResponse responseBuilder = GenericResponse.builder()
                            .message(mappedMessage)
                            .origin(exchange.getRequest().getURI().getPath())
                            .success(false)
                            .timestamp(LocalDateTime.now())
                            .errorDetails(errorDetails)
                            .requestId(transactionId).build();

                    if (exceptionMessage != null) {
                        responseBuilder.setMessage(exceptionMessage);
                    }

                    return responseBuilder;
                })
                .map(body -> new ResponseEntity<>(body, status));
    }
}
