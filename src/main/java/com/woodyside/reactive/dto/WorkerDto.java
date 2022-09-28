package com.woodyside.reactive.dto;

import com.woodyside.reactive.model.Department;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkerDto {

    private String id;

    private Integer experience;

    private List<String> skills;

    private Department department;

    private Integer age;

    private BigDecimal salary;
}
