package com.woodyside.reactive.util.mapper;

import com.woodyside.reactive.TraceUnitRule;
import com.woodyside.reactive.dto.WorkerDto;
import com.woodyside.reactive.model.Department;
import com.woodyside.reactive.model.Worker;
import com.woodyside.reactive.util.DtoConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(value = MockitoExtension.class)
@ExtendWith(value = TraceUnitRule.class)
public class WorkerMapperTest {

    @Test
    public void shouldReturnToDto() {

        Worker worker = Worker.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        WorkerDto workerDto = DtoConverter.modelToDto(worker);

        assertNotNull(workerDto);
        assertEquals(worker.getId(),workerDto.getId());
        assertEquals(worker.getExperience(), workerDto.getExperience());
        assertEquals(worker.getSalary(), workerDto.getSalary());
        assertEquals(worker.getExperience(), workerDto.getExperience());
        assertEquals(worker.getSkills(), workerDto.getSkills());

    }

    @Test
    public void shouldReturnFromDto() {

        WorkerDto workerDto = WorkerDto.builder()
                .id("3d3ic3cn3icn39sdcjk3cj34c")
                .skills(List.of("Java", "Spring", "SQL"))
                .department(Department.IT)
                .experience(5)
                .salary(BigDecimal.valueOf(300.0))
                .age(32)
                .build();

        Worker worker = DtoConverter.dtoToModel(workerDto);

        assertNotNull(workerDto);
        assertEquals(worker.getId(),workerDto.getId());
        assertEquals(worker.getExperience(), workerDto.getExperience());
        assertEquals(worker.getSalary(), workerDto.getSalary());
        assertEquals(worker.getExperience(), workerDto.getExperience());
        assertEquals(worker.getSkills(), workerDto.getSkills());
    }
}
