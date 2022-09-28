package com.woodyside.reactive.config;

import com.woodyside.reactive.model.audit.ReactiveAuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories("com.woodyside.reactive.repository")
@EnableReactiveMongoAuditing
public class MongoDBConfig {

    @Bean
    public ReactiveAuditorAware<String> myAuditorProvider() {
        return new ReactiveAuditorAwareImpl();
    }
}
