package com.woodyside.reactive.repository;

import com.woodyside.reactive.TraceUnitRule;
import com.woodyside.reactive.model.Department;
import com.woodyside.reactive.model.Worker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Range;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataMongoTest
@ExtendWith(value = SpringExtension.class)
@ExtendWith(value = TraceUnitRule.class)
public class WorkerRepositoryTest {

    @Autowired
    private WorkerRepository workerRepository;

    @Test
    @DisplayName(value = "# Save method test")
    @Tag(value = "Worker repository test")
    public void whenSave_thenReturnSavedWorker() {

        Worker worker1 = Worker.builder()
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        Publisher<Worker> setup = workerRepository.deleteAll()
                .thenMany(workerRepository.insert(worker1));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "# Find workers by age between method test")
    @Tag(value = "Worker repository test")
    public void whenFindWorkersByAgeBetween_returnWorkers() {

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

        Publisher<Worker> setup = workerRepository.deleteAll()
                .thenMany(workerRepository.insert(List.of(worker1,worker2)));

        Flux<Worker> found = workerRepository
                .findByAgeBetween(Range.closed(30,35));

        Publisher<Worker> composite = Flux
                .from(setup)
                .thenMany(found);

        StepVerifier
                .create(composite)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "# Find worker by id method test")
    @Tag(value = "Worker repository test")
    public void whenFindWorkerById_thenReturnWorker() {

        Worker worker1 = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        Publisher<Worker> setup = workerRepository.deleteAll()
                .thenMany(workerRepository.save(worker1));

        Mono<Worker> find = workerRepository.findById(worker1.getId());

        Publisher<Worker> composite = Mono
                .from(setup)
                .then(find);

        StepVerifier
                .create(composite)
                .consumeNextWith(account -> {
                    assertNotNull(worker1.getId());
                    assertEquals(worker1.getDepartment().name(), Department.IT.name());
                    assertEquals(worker1.getSalary(), BigDecimal.valueOf(300.0));
                    assertEquals(worker1.getAge(), 32);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "# Find all workers test method")
    @Tag(value = "Worker repository test")
    public void whenFindAllWorker_thenReturnAllWorkers() {

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

        Publisher<Worker> setup = workerRepository.deleteAll()
                .thenMany(workerRepository.insert(List.of(worker1,worker2)));

        Flux<Worker> found = workerRepository
                .findAll();

        Publisher<Worker> composite = Flux
                .from(setup)
                .thenMany(found);

        StepVerifier
                .create(composite)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "# Delete worker test method")
    @Tag(value = "Worker repository test")
    public void whenDeleteWorker_then_ReturnNothing() {

        Worker worker1 = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        Publisher<Worker> setup = workerRepository.deleteAll()
                .then(workerRepository.insert(worker1));

        Mono<Void> deleted = workerRepository.deleteById(worker1.getId());

        Publisher<Void> composite = Mono
                .from(setup)
                .then(deleted);

        StepVerifier
                .create(composite)
                .expectNextCount(0)
                .verifyComplete();
    }
}
