package com.woodyside.reactive.service;

import com.woodyside.reactive.dto.WorkerDto;
import com.woodyside.reactive.exception.NoWorkerFoundException;
import com.woodyside.reactive.repository.WorkerRepository;
import com.woodyside.reactive.util.DtoConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkerService {

    private final WorkerRepository workerRepository;

    public Flux<WorkerDto> getAllWorkers() {
        return workerRepository.findAll()
                .map(DtoConverter::modelToDto);
    }

    public Mono<WorkerDto> getWorkerById(String workerId) {
        return workerRepository.findById(workerId).map(DtoConverter::modelToDto)
                .switchIfEmpty(Mono.error(NoWorkerFoundException::new));
    }

    public Flux<WorkerDto> getWorkersWithAgeRange(Integer from, Integer to) {
        return workerRepository.findByAgeBetween(Range.closed(from,to))
                .switchIfEmpty(Mono.error(NoWorkerFoundException::new));
    }

    public Mono<WorkerDto> registerNewWorker(Mono<WorkerDto> newWorker) {
        return newWorker.map(DtoConverter::dtoToModel)
                .flatMap(workerRepository::insert)
                .map(DtoConverter::modelToDto);
    }

    public Mono<WorkerDto> updateWorkerById(Mono<WorkerDto> updatedWorker, String workerId) {
        return workerRepository.findById(workerId)
                .flatMap(w -> updatedWorker.map(DtoConverter::dtoToModel))
                .doOnNext(w -> w.setId(workerId))
                .flatMap(workerRepository::save)
                .map(DtoConverter::modelToDto)
                .switchIfEmpty(Mono.error(NoWorkerFoundException::new));
    }

    public Mono<Void> deleteWorker(String workerId) {
        return workerRepository.deleteById(workerId);
    }
}
