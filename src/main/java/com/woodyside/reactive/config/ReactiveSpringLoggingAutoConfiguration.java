package com.woodyside.reactive.config;

import com.woodyside.reactive.filter.ReactiveSpringLoggingFilter;
import com.woodyside.reactive.util.UniqueIDGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReactiveSpringLoggingAutoConfiguration {

    private String ignorePatterns;
    private boolean logHeaders;
    private boolean useContentLength;

    @Bean
    public UniqueIDGenerator generator() {
        return new UniqueIDGenerator();
    }

    @Bean
    public ReactiveSpringLoggingFilter reactiveSpringLoggingFilter() {
        return new ReactiveSpringLoggingFilter(generator(), ignorePatterns, logHeaders, useContentLength);
    }
}