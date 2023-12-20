package com.issa.education.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CourseEnrollException extends RuntimeException {

    public CourseEnrollException(String message) {
        super(message);
    }
}
