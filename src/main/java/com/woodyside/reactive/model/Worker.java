package com.woodyside.reactive.model;

import com.woodyside.reactive.model.audit.AuditMetadata;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "workers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Worker extends AuditMetadata {

    @Id
    private String id;

    @Field(value = "worker_experience")
    private Integer experience;

    @Field(value = "worker_skills")
    private List<String> skills;

    @Field(value = "worker_department")
    private Department department;

    @Field(value = "worker_age")
    private Integer age;

    @Field(value = "worker_salary")
    private BigDecimal salary;
}
