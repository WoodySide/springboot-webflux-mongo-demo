package com.woodyside.reactive.repository;

import com.woodyside.reactive.dto.WorkerDto;
import com.woodyside.reactive.model.Worker;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface WorkerRepository extends ReactiveMongoRepository<Worker, String> {

    Flux<Worker> findByAgeBetween(Range<Integer> ageRange);
}
