package com.woodyside.reactive.service;

import com.woodyside.reactive.TraceUnitRule;
import com.woodyside.reactive.dto.WorkerDto;
import com.woodyside.reactive.exception.NoWorkerFoundException;
import com.woodyside.reactive.model.Department;
import com.woodyside.reactive.model.Worker;
import com.woodyside.reactive.repository.WorkerRepository;
import com.woodyside.reactive.util.DtoConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Range;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(value = SpringExtension.class)
@ExtendWith(value = TraceUnitRule.class)
public class WorkerServiceTest {

    @InjectMocks
    private WorkerService workerService;

    @Mock
    private WorkerRepository workerRepository;

    @Test
    @DisplayName(value = "# Find all worker service test method")
    @Tag(value = "Worker_service_test")
    public void whenGetAllWorkers_thenReturnAllWorker() {

        Worker worker1 = Worker.builder()
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        Worker worker2 = Worker.builder()
                .skills(List.of("HH", "Word", "Excel"))
                .department(Department.HR)
                .experience(7)
                .salary(BigDecimal.valueOf(180.0))
                .age(28)
                .build();

        when(workerRepository.findAll()).thenReturn(Flux.just(worker1,worker2));

        Flux<WorkerDto> workerMono = workerService.getAllWorkers();

        StepVerifier
                .create(workerMono)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "# Find all workers by age service test method")
    @Tag(value = "Worker_service_test")
    public void whenFindAllWorkersByAge_thenReturnAllWorkerAccordingCondition() {

        Worker worker1 = Worker.builder()
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        when(workerRepository.findByAgeBetween(Range.closed(30,35))).thenReturn(Flux.just(worker1));

        Flux<WorkerDto> workerMono = workerService.getWorkersWithAgeRange(30,35);

        StepVerifier
                .create(workerMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "# Register worker service test method")
    @Tag(value = "Worker_service_test")
    public void whenRegisterWorker_thenReturnWorker() {

        Worker newWorker = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        WorkerDto workerDto = DtoConverter.modelToDto(newWorker);

        when(workerRepository.insert(any(Worker.class))).thenReturn(Mono.just(newWorker));

        Mono<WorkerDto> created = workerService.registerNewWorker(Mono.just(workerDto));

        StepVerifier
                .create(created)
                .consumeNextWith(worker -> {
                    assertNotNull(worker.getId());
                    assertNotNull(worker.getDepartment());
                    assertNotNull(worker.getAge());
                    assertNotNull(worker.getSkills());
                    assertNotNull(worker.getSalary());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "# Update worker by id service test method")
    @Tag(value = "Worker_service_test")
    public void whenUpdateWorkerByIdWorker_thenReturnUpdatedWorker() {

        Worker oldWorker = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        Worker updatedWorker = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL", "RabbitMQ"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        WorkerDto workerDto = DtoConverter.modelToDto(updatedWorker);

        when(workerRepository.findById(anyString())).thenReturn(Mono.just(oldWorker));
        when(workerRepository.save(any(Worker.class))).thenReturn(Mono.just(updatedWorker));

        Mono<WorkerDto> workerMono = workerService.updateWorkerById(Mono.just(workerDto),oldWorker.getId());

        StepVerifier
                .create(workerMono)
                .consumeNextWith(worker -> {
                    assertNotNull(worker.getId());
                    assertEquals(worker.getDepartment().name(), Department.IT.name());
                    assertEquals(worker.getSkills(), List.of("Java", "Spring", "SQL", "RabbitMQ"));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "# Delete worker by id service test method")
    @Tag(value = "Worker_service_test")
    public void whenDeleteWorkerByIdWorker_thenReturnNothing() {

        Worker worker1 = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        when(workerRepository.deleteById(worker1.getId())).thenReturn(Mono.empty());

        Mono<Void> voidMono = workerService.deleteWorker(worker1.getId());

        StepVerifier
                .create(voidMono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "# Find worker by id service test method")
    @Tag(value = "Worker_service_test")
    public void whenGetWorkerById_thenReturnWorker() {

        Worker worker1 = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        when(workerRepository.findById(worker1.getId())).thenReturn(Mono.just(worker1));

        Mono<WorkerDto> workerMono = workerService.getWorkerById(worker1.getId());

        StepVerifier
                .create(workerMono)
                .consumeNextWith(foundWorker -> {
                    assertEquals(foundWorker.getDepartment().name(), Department.IT.name());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "# Find worker by non existent id service test method")
    @Tag(value = "Worker_service_test")
    public void whenGetWorkerByNonExistentId_thenReturnNothing() {

        when(workerRepository.findById("5d3it5cn3icn40sdcjk3cj34c")).thenReturn(Mono.empty());

        Mono<WorkerDto> workerMono = workerService.getWorkerById("5d3it5cn3icn40sdcjk3cj34c");

        StepVerifier
                .create(workerMono)
                .expectErrorMatches(throwable -> throwable instanceof NoWorkerFoundException)
                .verify();
    }
}
