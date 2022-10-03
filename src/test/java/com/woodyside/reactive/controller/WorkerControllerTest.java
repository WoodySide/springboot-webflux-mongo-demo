package com.woodyside.reactive.controller;

import com.woodyside.reactive.TraceUnitRule;
import com.woodyside.reactive.dto.WorkerDto;
import com.woodyside.reactive.model.Department;
import com.woodyside.reactive.model.Worker;
import com.woodyside.reactive.repository.WorkerRepository;
import com.woodyside.reactive.service.WorkerService;
import com.woodyside.reactive.util.DtoConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Range;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(value = SpringExtension.class)
@ExtendWith(value = TraceUnitRule.class)
@WebFluxTest(value = WorkerController.class)
@Import(value = WorkerService.class)
public class WorkerControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private WorkerRepository workerRepository;

    private static final String BASIS_URL = "/api/v1/workers";

    @Test
    @DisplayName(value = "# Create worker controller test method")
    @Tag(value = "Worker_controller_test")
    public void whenCreateWorker_theReturnCreated() {

        Worker newWorker = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        WorkerDto payload = DtoConverter.modelToDto(newWorker);

        when(workerRepository.insert(any(Worker.class))).thenReturn(Mono.just(newWorker));

        webClient
                .post()
                .uri(BASIS_URL + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(payload))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.experience").isNumber()
                .jsonPath("$.skills").isArray()
                .jsonPath("$.department").isNotEmpty()
                .jsonPath("$.age").isNumber()
                .jsonPath("$.salary").isNotEmpty();

        verify(workerRepository, times(1)).insert(any(Worker.class));
    }

    @Test
    @DisplayName(value = "# Find worker by id controller test method")
    @Tag(value = "Worker_controller_test")
    public void whenGetWorkerById_theReturnOk() {

        Worker found = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        when(workerRepository.findById(anyString())).thenReturn(Mono.just(found));

        webClient
                .get()
                .uri(BASIS_URL + "/" + "{id}", found.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.experience").isNumber()
                .jsonPath("$.skills").isArray()
                .jsonPath("$.department").isNotEmpty()
                .jsonPath("$.age").isNumber()
                .jsonPath("$.salary").isNotEmpty();

        verify(workerRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName(value = "# Find worker by non existent id controller test method")
    @Tag(value = "Worker_controller_test")
    public void whenGetWorkerByNonExistentId_theReturnNotFound() {

        when(workerRepository.findById(anyString())).thenReturn(Mono.empty());

        webClient
                .get()
                .uri(BASIS_URL + "/" + "{id}", "workerId")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @DisplayName(value = "# Find workers by range controller test method")
    @Tag(value = "Worker_controller_test")
    public void whenGetWorkersByRange_theReturnOk() {

        Worker worker1 = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        Worker worker2 = Worker.builder()
                .id("9d4cks3cn3rjfn40sdcjk6cj34c")
                .skills(List.of("HH", "LinkedIn", "Telegram"))
                .department(Department.HR)
                .experience(3)
                .salary(BigDecimal.valueOf(150.0))
                .age(27)
                .build();

        when(workerRepository.insert(List.of(worker1,worker2))).thenReturn(Flux.just(worker1,worker2));

        when(workerRepository.findByAgeBetween(Range.closed(25,28))).thenReturn(Flux.just(worker2));

        webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                .path(BASIS_URL + "/age-range")
                .queryParam("from", 25)
                .queryParam("to", 28)
                .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id").isNotEmpty()
                .jsonPath("$[0].experience").isEqualTo(3)
                .jsonPath("$[0].skills").isArray()
                .jsonPath("$[0].department").isEqualTo(Department.HR.name())
                .jsonPath("$[0].age").isEqualTo(27)
                .jsonPath("$[0].salary").isEqualTo(BigDecimal.valueOf(150.0));

        verify(workerRepository, times(1)).findByAgeBetween(Range.closed(25,28));
    }

    @Test
    @DisplayName(value = "# Delete worker by id controller test method")
    @Tag(value = "Worker_controller_test")
    public void whenDeleteWorkerById_theReturnNoContent() {

        Worker worker = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        when(workerRepository.insert(any(Worker.class))).thenReturn(Mono.just(worker));

        webClient
                .delete()
                .uri(BASIS_URL + "/delete/" + "{id}", worker.getId())
                .exchange()
                .expectStatus()
                .isNoContent();

        verify(workerRepository, times(1)).deleteById(worker.getId());
    }

    @Test
    @DisplayName(value = "# Update worker by id controller test method")
    @Tag(value = "Worker_controller_test")
    public void whenUpdateWorkerById_theReturnOk() {

        Worker worker = Worker.builder()
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        when(workerRepository.findById(anyString())).thenReturn(Mono.just(worker));

        Worker updatedWorker = Worker.builder()
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(10)
                .salary(BigDecimal.valueOf(300.0))
                .age(40)
                .build();

        when(workerRepository.save(any(Worker.class))).thenReturn(Mono.just(updatedWorker));

        webClient
                .put()
                .uri(BASIS_URL + "/update/" + worker.getId())
                .body(BodyInserters.fromValue(updatedWorker))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.age").isEqualTo(40)
                .jsonPath("$.experience").isEqualTo(10);
    }
}
