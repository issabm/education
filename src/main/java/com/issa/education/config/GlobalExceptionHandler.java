package com.issa.education.config;

import com.issa.education.web.rest.errors.CourseEnrollException;
import com.issa.education.web.rest.errors.CourseException;
import com.issa.education.web.rest.errors.StudentException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {

        return new ResponseEntity<>(e.getMessage() , HttpStatus.CONFLICT);
    }


    @ExceptionHandler(CourseEnrollException.class)
    public ResponseEntity<String> handleCourseEnrollException(CourseEnrollException e) {

        return new ResponseEntity<>(e.getMessage()  , HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CourseException.class)
    public ResponseEntity<String> handleCourseException(CourseException e) {

        return new ResponseEntity<>( e.getMessage() , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentException.class)
    public ResponseEntity<String> handleStudentException(StudentException e) {

        return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
    }
}