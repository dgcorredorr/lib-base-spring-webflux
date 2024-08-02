package com.fstech.common.utils.filter;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fstech.application.service.TraceabilityService;
import com.fstech.common.utils.ServiceLogger;
import com.fstech.common.utils.enums.LogLevel;
import com.fstech.common.utils.enums.Task;
import com.fstech.common.utils.enums.TraceabilityStatus;
import com.fstech.common.utils.enums.TraceabilityTask;
import com.fstech.core.entity.Traceability;

import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

/**
 * Filtro de solicitudes y respuestas HTTP que realiza acciones antes del
 * procesamiento de la solicitud
 * y después del procesamiento de la respuesta.
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

    private final ServiceLogger<HttpFilter> logger;
    private final TraceabilityService traceabilityService;

    public HttpFilter(ServiceLogger<HttpFilter> logger, TraceabilityService traceabilityService) {
        logger.setLoggerClass(HttpFilter.class);
        this.logger = logger;
        this.traceabilityService = traceabilityService;
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        long startTime = System.currentTimeMillis();

        // Log y trazabilidad de la solicitud
        return exchange.getRequest().getBody().collectList().flatMap(body -> {
            processRequestBody(exchange, body);

            // Continuar con el procesamiento de la solicitud y manejar la respuesta
            return chain.filter(exchange)
                    .doFinally(signalType -> {
                        long endTime = System.currentTimeMillis();
                        ContextFilter.setCurrentExchange(exchange);
                        processResponseBody(exchange, signalType, endTime - startTime, body);
                    });
        });
    }

    private void processRequestBody(ServerWebExchange exchange, Object body) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = exchange.getLogPrefix();

        traceabilityService.createTraceability(Traceability.builder()
                .transactionId(requestId)
                .status(TraceabilityStatus.SUCCESS)
                .origin(request.getPath().pathWithinApplication().value())
                .method(request.getMethod())
                .task(TraceabilityTask.START_REQUEST)
                .request(body.toString())
                .build()).then();

        logger.log("Entrada Principal - " + request.getPath().pathWithinApplication().value(),
                Task.HTTP_REQUEST_FILTER,
                LogLevel.INFO,
                body,
                null);
    }

    private void processResponseBody(ServerWebExchange exchange, SignalType signalType, long duration,
            Object requestBody) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String transactionId = exchange.getLogPrefix();
        TraceabilityStatus traceabilityStatus = exchange.getAttribute("TRACEABILITY_STATUS");
        HttpStatusCode httpStatusCode = response.getStatusCode();
        LogLevel logLevel = (httpStatusCode != null) ? httpStatusCode.is2xxSuccessful() ? LogLevel.INFO
                : httpStatusCode.is4xxClientError() ? LogLevel.WARNING : LogLevel.ERROR : LogLevel.ERROR;

        traceabilityService.createTraceability(Traceability.builder()
                .transactionId(transactionId)
                .status(traceabilityStatus != null ? traceabilityStatus : TraceabilityStatus.SUCCESS)
                .origin(request.getPath().pathWithinApplication().value())
                .method(request.getMethod())
                .task(TraceabilityTask.END_REQUEST)
                .request(requestBody.toString())
                .response(response.toString())
                .durationMillis(duration)
                .build()).then();

        logger.log("Salida Principal - " + request.getPath().pathWithinApplication().value(),
                Task.HTTP_RESPONSE_FILTER,
                logLevel,
                response,
                duration);
    }

}
