package com.woodyside.reactive.filter;

import com.woodyside.reactive.interceptor.RequestLoggingInterceptor;
import com.woodyside.reactive.interceptor.ResponseLoggingInterceptor;
import com.woodyside.reactive.util.UniqueIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.value;

@Slf4j
public class ReactiveSpringLoggingFilter implements WebFilter {

     private final UniqueIDGenerator generator;
     private final String ignorePatterns;
     private final boolean logHeaders;
     private final boolean useContentLength;

     public ReactiveSpringLoggingFilter(UniqueIDGenerator generator, String ignorePatterns, boolean logHeaders, boolean useContentLength) {
            this.generator = generator;
            this.ignorePatterns = ignorePatterns;
            this.logHeaders = logHeaders;
            this.useContentLength = useContentLength;
        }

     @Override
     public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

            if (ignorePatterns != null && exchange.getRequest().getURI().getPath().matches(ignorePatterns)) {
                return chain.filter(exchange);
            } else {
                generator.generateAndSetMDC(exchange.getRequest());
                final long startTime = System.currentTimeMillis();
                List<String> header = exchange.getRequest().getHeaders().get("Content-Length");
                if (useContentLength && (header == null || header.get(0).equals("0"))) {
                    if (logHeaders)
                        log.info("Request: method={}, uri={}, headers={}, audit={}", exchange.getRequest().getMethod(),
                                exchange.getRequest().getURI().getPath(), exchange.getRequest().getHeaders(), value("audit", true));
                    else
                        log.info("Request: method={}, uri={}, audit={}", exchange.getRequest().getMethod(),
                                exchange.getRequest().getURI().getPath(), value("audit", true));
                }
                ServerWebExchangeDecorator exchangeDecorator = new ServerWebExchangeDecorator(exchange) {
                    @Override
                    public ServerHttpRequest getRequest() {
                        return new RequestLoggingInterceptor(super.getRequest(), logHeaders);
                    }

                    @Override
                    public ServerHttpResponse getResponse() {
                        return new ResponseLoggingInterceptor(super.getResponse(), startTime, logHeaders);
                    }
                };
                return chain.filter(exchangeDecorator)
                        .doOnSuccess(aVoid -> {
                            logResponse(startTime, exchangeDecorator.getResponse(), exchangeDecorator.getResponse().getStatusCode().value());
                        })
                        .doOnError(throwable -> {
                            logResponse(startTime, exchangeDecorator.getResponse(), 500);
                        });
            }
        }

    private void logResponse(long startTime, ServerHttpResponse response, int overriddenStatus) {
        final long duration = System.currentTimeMillis() - startTime;
        List<String> header = response.getHeaders().get("Content-Length");
        if (useContentLength && (header == null || header.get(0).equals("0"))) {
            if (logHeaders)
                log.info("Response({} ms): status={}, headers={}, audit={}", value("X-Response-Time", duration),
                        value("X-Response-Status", overriddenStatus), response.getHeaders(), value("audit", true));
            else
                log.info("Response({} ms): status={}, audit={}", value("X-Response-Time", duration),
                        value("X-Response-Status", overriddenStatus), value("audit", true));
        }
    }
}
