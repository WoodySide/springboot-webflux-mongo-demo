package com.woodyside.reactive.controller;

import com.woodyside.reactive.dto.WorkerDto;
import com.woodyside.reactive.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "api/v1/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<WorkerDto> getAllWorkers() {
        return workerService.getAllWorkers();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<WorkerDto> getWorkerById(@PathVariable(value = "id") String workerId) {
        return workerService.getWorkerById(workerId);
    }

    @GetMapping(path = "/age-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<WorkerDto> getWorkersWithAgeRange(@RequestParam(value = "from") Integer from, @RequestParam(value = "to") Integer to) {
        return workerService.getWorkersWithAgeRange(from,to);
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<WorkerDto> registerNewWorker(@RequestBody Mono<WorkerDto> newWorker) {
        return workerService.registerNewWorker(newWorker);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<WorkerDto> updateWorkerById(@RequestBody Mono<WorkerDto> updateWorker, @PathVariable(value = "id") String workerId) {
        return workerService.updateWorkerById(updateWorker,workerId);
    }

    @DeleteMapping(value = "/delete/{id}")
    public Mono<Void> deleteWorkerById(@PathVariable(value = "id") String workerId) {
        return workerService.deleteWorker(workerId);
    }
}
