package com.woodyside.reactive.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;

import static net.logstash.logback.argument.StructuredArguments.value;

@Slf4j
public class RequestLoggingInterceptor extends ServerHttpRequestDecorator {

    private final boolean logHeader;

    public RequestLoggingInterceptor(ServerHttpRequest delegate, boolean logHeaders) {
        super(delegate);
        this.logHeader = logHeaders;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return super.getBody().doOnNext(dataBuffer -> {
            try {
                Channels.newChannel(baos).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                String body = IOUtils.toString(baos.toByteArray(), "UTF-8");
                log.info("Request: methods = {}, uri = {}, payload = {}, audit = {}", getDelegate().getMethod(),
                        getDelegate().getPath(), body, value("audit", true));

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
