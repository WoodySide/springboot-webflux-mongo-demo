package com.woodyside.reactive.model.audit;

import org.springframework.data.domain.ReactiveAuditorAware;
import reactor.core.publisher.Mono;


public class ReactiveAuditorAwareImpl implements ReactiveAuditorAware<String> {

    @Override
    public Mono<String> getCurrentAuditor() {
        return Mono.just("Woodyside");
    }
}
