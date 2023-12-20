package com.issa.education.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentException extends RuntimeException {

    public StudentException(String message) {
        super(message);
    }
}