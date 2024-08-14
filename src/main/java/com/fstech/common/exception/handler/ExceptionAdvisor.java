package com.fstech.common.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

import com.fstech.application.dto.GenericResponseDto;
import com.fstech.application.service.MessageService;
import com.fstech.application.service.ServiceErrorService;
import com.fstech.application.service.TraceabilityService;
import com.fstech.common.exception.ServiceException;
import com.fstech.common.utils.enums.LogLevel;
import com.fstech.common.utils.enums.MessageMapping;
import com.fstech.common.utils.enums.TraceabilityStatus;
import com.fstech.common.utils.log.ServiceLogger;
import com.fstech.common.utils.tasks.Task;
import com.fstech.common.utils.tasks.TaskService;
import com.fstech.common.utils.tasks.TraceabilityTaskService;
import com.fstech.core.entity.ServiceError;
import com.fstech.core.entity.Traceability;

import lombok.AllArgsConstructor;
import lombok.Data;
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

        protected final ServiceLogger<ExceptionAdvisor> logger = new ServiceLogger<>(ExceptionAdvisor.class);
        protected final TraceabilityService traceabilityService;
        protected final ServiceErrorService serviceErrorService;
        protected final MessageService messageService;
        protected final TaskService taskService;
        protected final TraceabilityTaskService traceabilityTaskService;

        protected static final String EXCEPTION_MANAGER = "EXCEPTION_MANAGER";

        public ExceptionAdvisor(TraceabilityService traceabilityService,
                        ServiceErrorService serviceErrorService, MessageService messageService,
                        TaskService taskService, TraceabilityTaskService traceabilityTaskService) {
                this.traceabilityService = traceabilityService;
                this.serviceErrorService = serviceErrorService;
                this.messageService = messageService;
                this.taskService = taskService;
                this.traceabilityTaskService = traceabilityTaskService;
        }

        @ExceptionHandler(ServiceException.class)
        public Mono<ResponseEntity<GenericResponseDto>> handleServiceException(ServiceException ex,
                        ServerWebExchange exchange) {
                ExceptionDetails details = new ExceptionDetails(ex, exchange, HttpStatus.CONFLICT,
                                TraceabilityStatus.FAILED, LogLevel.WARN, ex.getTask(), ex.getMessage(), null);
                return handleExceptionInternal(details);
        }

        @ExceptionHandler(WebExchangeBindException.class)
        public Mono<ResponseEntity<GenericResponseDto>> handleValidationException(WebExchangeBindException ex,
                        ServerWebExchange exchange) {
                String errorMessage = "Validation failed";
                HttpStatus status = HttpStatus.BAD_REQUEST;
                TraceabilityStatus traceabilityStatus = TraceabilityStatus.FAILED;
                LogLevel logLevel = LogLevel.WARN;

                Map<String, String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                                .collect(Collectors.groupingBy(
                                                FieldError::getField,
                                                Collectors.mapping(FieldError::getDefaultMessage,
                                                                Collectors.joining(", "))));

                ExceptionDetails details = new ExceptionDetails(ex, exchange,
                                status, traceabilityStatus, logLevel, taskService.getTaskById(EXCEPTION_MANAGER).get(),
                                errorMessage, validationErrors);
                return handleExceptionInternal(details);
        }

        @ExceptionHandler(MissingRequestValueException.class)
        public Mono<ResponseEntity<GenericResponseDto>> handleServerWebInputException(MissingRequestValueException ex,
                        ServerWebExchange exchange) {

                Map<String, String> validationErrors = new HashMap<>();
                validationErrors.put(ex.getName(), "Required " + ex.getLabel() + " is not present.");

                String errorMessage = "Missing or invalid request value.";
                ExceptionDetails details = new ExceptionDetails(ex, exchange,
                                HttpStatus.BAD_REQUEST, TraceabilityStatus.FAILED, LogLevel.WARN,
                                taskService.getTaskById(EXCEPTION_MANAGER).get(),
                                errorMessage, validationErrors);
                return handleExceptionInternal(details);
        }

        @ExceptionHandler(ServerWebInputException.class)
        public Mono<ResponseEntity<GenericResponseDto>> handleServerWebInputException(ServerWebInputException ex,
                        ServerWebExchange exchange) {
                String errorMessage = "Invalid input provided.";
                Map<String, String> validationErrors = new HashMap<>();
                validationErrors.put("message", "Type mismatch: " + ex.getMethodParameter());

                ExceptionDetails details = new ExceptionDetails(ex, exchange,
                                HttpStatus.BAD_REQUEST, TraceabilityStatus.FAILED, LogLevel.WARN,
                                taskService.getTaskById(EXCEPTION_MANAGER).get(),
                                errorMessage, validationErrors);

                return handleExceptionInternal(details);
        }

        @ExceptionHandler(Exception.class)
        public Mono<ResponseEntity<GenericResponseDto>> handleGenericException(Exception ex,
                        ServerWebExchange exchange) {
                ExceptionDetails details = new ExceptionDetails(ex, exchange, HttpStatus.INTERNAL_SERVER_ERROR,
                                TraceabilityStatus.ERROR, LogLevel.ERROR,
                                taskService.getTaskById(EXCEPTION_MANAGER).get(), null, null);
                return handleExceptionInternal(details);
        }

        protected Mono<ResponseEntity<GenericResponseDto>> handleExceptionInternal(ExceptionDetails details) {
                details.getExchange().getAttributes().put("TRACEABILITY_STATUS", details.getTraceabilityStatus());

                String transactionId = details.getExchange().getLogPrefix();
                Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put("exception", details.getEx().getClass().getName());
                errorDetails.put("message", details.getEx().getMessage());

                traceabilityService.createTraceability(Traceability.builder()
                                .transactionId(transactionId)
                                .task(traceabilityTaskService.getTaskById("REQUEST_ERROR").get())
                                .method(details.getExchange().getRequest().getMethod())
                                .status(details.getTraceabilityStatus())
                                .origin(details.getExchange().getRequest().getURI().getPath())
                                .request(details.getExchange().getRequest().toString())
                                .response(details.getExchange().getResponse().toString())
                                .build());

                serviceErrorService.createServiceError(ServiceError.builder()
                                .transactionId(transactionId)
                                .success(false)
                                .origin(details.getExchange().getRequest().getURI().getPath())
                                .method(details.getExchange().getRequest().getMethod())
                                .task(details.getTask())
                                .errorClass(details.getEx() instanceof ServiceException
                                                ? ((ServiceException) details.getEx()).getTask().getOrigin()
                                                                .getOriginClass()
                                                : "")
                                .errorMethod(details.getEx() instanceof ServiceException
                                                ? ((ServiceException) details.getEx()).getTask().getOrigin()
                                                                .getOriginMethod()
                                                : "")
                                .message(details.getEx().getMessage())
                                .stackTrace(Arrays.toString(details.getEx().getStackTrace()))
                                .request(details.getExchange().getRequest().toString())
                                .response(details.getExchange().getResponse().toString())
                                .build());

                logger.log("Excepción - " + details.getExchange().getRequest().getURI().getPath(),
                                taskService.getTaskById(EXCEPTION_MANAGER).get(), details.getLogLevel(),
                                details.getEx(), null);

                return createErrorResponse(details, transactionId, errorDetails);
        }

        protected Mono<ResponseEntity<GenericResponseDto>> createErrorResponse(ExceptionDetails details,
                        String transactionId,
                        Map<String, Object> errorDetails) {

                String mappedMessage = messageService.mapMessage(MessageMapping.DEFAULT_ERROR);

                GenericResponseDto responseBuilder = GenericResponseDto.builder()
                                .message(mappedMessage)
                                .origin(details.getExchange().getRequest().getURI().getPath())
                                .success(false)
                                .timestamp(LocalDateTime.now())
                                .errorDetails(errorDetails)
                                .validationErrors(details.getValidationErrors())
                                .requestId(transactionId).build();

                if (details.getExceptionMessage() != null) {
                        responseBuilder.setMessage(details.getExceptionMessage());
                }

                return Mono.just(new ResponseEntity<>(responseBuilder, details.getStatus()));
        }

        @Data
        @AllArgsConstructor
        protected class ExceptionDetails {
                private final Exception ex;
                private final ServerWebExchange exchange;
                private final HttpStatus status;
                private final TraceabilityStatus traceabilityStatus;
                private final LogLevel logLevel;
                private final Task task;
                private final String exceptionMessage;
                private final Map<String, String> validationErrors;
        }
}
