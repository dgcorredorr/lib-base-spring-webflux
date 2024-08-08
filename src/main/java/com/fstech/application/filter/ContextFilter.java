package com.fstech.application.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ContextFilter implements WebFilter {

    private static final ThreadLocal<ServerWebExchange> exchangeHolder = new ThreadLocal<>();

    public static ServerWebExchange getCurrentExchange() {
        return exchangeHolder.get();
    }

    public static void setCurrentExchange(ServerWebExchange exchange) {
        exchangeHolder.set(exchange);
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(ServerWebExchange.class, exchange))
                .doFinally(signalType -> exchangeHolder.remove());
    }
}
