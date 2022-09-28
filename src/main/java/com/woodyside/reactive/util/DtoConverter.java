package com.woodyside.reactive.util;

import com.woodyside.reactive.dto.WorkerDto;
import com.woodyside.reactive.model.Worker;
import org.springframework.beans.BeanUtils;

public class DtoConverter {

    public static WorkerDto modelToDto(Worker worker) {
        WorkerDto workerDto = WorkerDto.builder().build();
        BeanUtils.copyProperties(worker, workerDto);
        return workerDto;
    }

    public static Worker dtoToModel(WorkerDto workerDto) {
        Worker worker = Worker.builder().build();
        BeanUtils.copyProperties(workerDto, worker);
        return worker;
    }
}
