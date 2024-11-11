package com.meli.application.filter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.meli.application.service.TraceabilityService;
import com.meli.common.utils.enums.LogLevel;
import com.meli.common.utils.enums.TraceabilityStatus;
import com.meli.common.utils.log.ServiceLogger;
import com.meli.common.utils.tasks.TaskService;
import com.meli.common.utils.tasks.TraceabilityTaskService;
import com.meli.core.entity.Traceability;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Filtro de solicitudes y respuestas HTTP que realiza acciones antes del
 * procesamiento de la solicitud y después del procesamiento de la respuesta.
 *
 * <p>
 * Este filtro se utiliza para:
 * </p>
 * <ul>
 * <li>Generar y asignar un identificador único (requestId) a cada
 * solicitud.</li>
 * <li>Registrar el tiempo de inicio y fin de procesamiento de la
 * solicitud.</li>
 * <li>Loguear detalles de la solicitud y la respuesta.</li>
 * <li>Crear un registro de trazabilidad para cada solicitud y respuesta.</li>
 * </ul>
 */
@Component
public class HttpFilter implements WebFilter {

    private final ServiceLogger<HttpFilter> logger = new ServiceLogger<>(HttpFilter.class);
    private final TraceabilityService traceabilityService;
    private final TaskService taskService;
    private final TraceabilityTaskService traceabilityTaskService;

    public HttpFilter(TraceabilityService traceabilityService, TaskService taskService,
            TraceabilityTaskService traceabilityTaskService) {
        this.traceabilityService = traceabilityService;
        this.taskService = taskService;
        this.traceabilityTaskService = traceabilityTaskService;
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        long startTime = System.currentTimeMillis();

        // AtomicReference para mantener el cuerpo de la solicitud
        AtomicReference<String> requestBodyRef = new AtomicReference<>("");
        AtomicReference<String> responseBodyRef = new AtomicReference<>("");

        ServerHttpRequest request = exchange.getRequest();
        return DataBufferUtils.join(request.getBody())
                .defaultIfEmpty(exchange.getResponse().bufferFactory().wrap(new byte[0])) // Handle empty body
                .flatMap(dataBuffer -> {
                    // Convertir el buffer a string
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer); // Release data buffer
                    String bodyString = new String(bytes, StandardCharsets.UTF_8);
                    requestBodyRef.set(bodyString);

                    // Crear un nuevo DataBuffer a partir del byte array
                    DataBuffer newDataBuffer = exchange.getResponse().bufferFactory().wrap(bytes);

                    // Decorar el request con el nuevo DataBuffer
                    ServerHttpRequest decoratedRequest = new ServerHttpRequestDecorator(request) {
                        @Override
                        @NonNull
                        public Flux<DataBuffer> getBody() {
                            return Flux.just(newDataBuffer);
                        }
                    };

                    // Decorar el response para capturar el cuerpo de la respuesta
                    ServerHttpResponse decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
                        @Override
                        @NonNull
                        public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                            return DataBufferUtils.join(body)
                                    .flatMap(buffer -> {
                                        byte[] responseBytes = new byte[buffer.readableByteCount()];
                                        buffer.read(responseBytes);
                                        DataBufferUtils.release(buffer); // Release data buffer
                                        String responseBodyString = new String(responseBytes, StandardCharsets.UTF_8);
                                        responseBodyRef.set(responseBodyString);

                                        // Crear un nuevo DataBuffer a partir del byte array
                                        DataBuffer newResponseDataBuffer = exchange.getResponse().bufferFactory()
                                                .wrap(responseBytes);
                                        return super.writeWith(Flux.just(newResponseDataBuffer));
                                    });
                        }
                    };

                    // Crear un nuevo ServerWebExchange con el ServerHttpRequest y
                    // ServerHttpResponse decorados
                    ServerWebExchange mutatedExchange = exchange.mutate().request(decoratedRequest)
                            .response(decoratedResponse).build();

                    // Continuar con el procesamiento de la solicitud
                    return chain.filter(mutatedExchange)
                    .doOnSubscribe(subscription -> processRequestBody(exchange, bodyString))
                    .doFinally(signalType -> {
                        long endTime = System.currentTimeMillis();
                        String requestBody = requestBodyRef.get();
                        String responseBody = responseBodyRef.get();
                        ContextFilter.setCurrentExchange(mutatedExchange);
                        processResponseBody(mutatedExchange, endTime - startTime, requestBody, responseBody);
                    });
                });
    }

    private void processRequestBody(ServerWebExchange exchange, Object body) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = exchange.getLogPrefix();

        traceabilityService.createTraceability(Traceability.builder()
                .transactionId(requestId)
                .status(TraceabilityStatus.SUCCESS)
                .origin(request.getPath().toString())
                .method(request.getMethod())
                .task(traceabilityTaskService.getTaskById("START_REQUEST").get())
                .request(body.toString())
                .build());

        logger.log("Entrada Principal - " + request.getPath().pathWithinApplication().value(),
                taskService.getTaskById("HTTP_REQUEST_FILTER").get(),
                LogLevel.INFO,
                body.toString(),
                null);
    }

    private void processResponseBody(ServerWebExchange exchange, long duration,
            Object requestBody, Object responseBody) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String transactionId = exchange.getLogPrefix();
        TraceabilityStatus traceabilityStatus = exchange.getAttribute("TRACEABILITY_STATUS");
        HttpStatusCode httpStatusCode = response.getStatusCode();
        LogLevel logLevel;

        if (httpStatusCode != null) {
            if (httpStatusCode.is2xxSuccessful()) {
                logLevel = LogLevel.INFO;
            } else if (httpStatusCode.is4xxClientError()) {
                logLevel = LogLevel.WARN;
            } else {
                logLevel = LogLevel.ERROR;
            }
        } else {
            logLevel = LogLevel.ERROR;
        }
        traceabilityService.createTraceability(Traceability.builder()
                .transactionId(transactionId)
                .status(traceabilityStatus != null ? traceabilityStatus : TraceabilityStatus.SUCCESS)
                .origin(request.getPath().toString())
                .method(request.getMethod())
                .task(traceabilityTaskService.getTaskById("END_REQUEST").get())
                .request(requestBody.toString())
                .response(responseBody != null ? responseBody.toString() : "No response body")
                .durationMillis(duration)
                .build());

        logger.log("Salida Principal - " + request.getPath().pathWithinApplication().value(),
                taskService.getTaskById("HTTP_RESPONSE_FILTER").get(),
                logLevel,
                responseBody != null ? responseBody.toString() : "No response body",
                duration);
    }

}
