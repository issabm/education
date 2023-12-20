package com.issa.education.web.rest;

import com.issa.education.domain.Student;
import com.issa.education.service.StudentService;
import com.issa.education.web.rest.errors.StudentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/student")
public class StudentResource {


    @Autowired
    private StudentService studentService;



    /**
     * {@code POST  /register} : Register new Student.
     *
     * @param student the student to register.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new course, or with status {@code 400 (Bad Request)} if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/register")
    public ResponseEntity<Student> registerUser(@RequestBody Student student) {
        Student registredStdent = studentService.registerOne(student);
        return new ResponseEntity<>(registredStdent, HttpStatus.CREATED);
    }

    // Exception handling for InvalidInputException
    @ExceptionHandler(StudentException.class)
    public ResponseEntity<String> handleInvalidInputException(StudentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /**
     * {@code DELETE  /student/:id} : delete the "id" student.
     *
     * @param id the id of the student to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id)   throws URISyntaxException {
        studentService.deleteOne(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
