package com.woodyside.reactive.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoWorkerFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "No worker found!";

    public NoWorkerFoundException() {
        super(ERROR_MESSAGE);
    }
}
