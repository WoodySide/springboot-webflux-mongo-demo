package com.woodyside.reactive.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;

@Configuration
public class ReactiveExceptionHandlingConfig {

    @Bean
    public ServerCodecConfigurer codecConfigurer() {
        return ServerCodecConfigurer.create();
    }

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }
}
