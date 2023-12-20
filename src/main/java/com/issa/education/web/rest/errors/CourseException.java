package com.issa.education.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseException extends RuntimeException {

    public CourseException(String message) {
        super(message);
    }
}
