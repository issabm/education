package com.issa.education.web.rest;

import com.issa.education.domain.Course;
import com.issa.education.domain.CourseEnroll;
import com.issa.education.domain.Student;
import com.issa.education.service.CourseEnrollService;
import com.issa.education.service.StudentService;
import com.issa.education.web.rest.errors.CourseEnrollException;
import com.issa.education.web.rest.errors.CourseException;
import com.issa.education.web.rest.errors.StudentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/course-enroll")
public class CourseEnrollResource {


    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseEnrollService courseEnrollService;

    /**
     * {@code POST /student/{id_student}/course{id_course} } : enroll student in course.
     *
     * @param idStudent the student id to enroll.
     * @param idCourse the course id to enroll.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new course, or with status {@code 400 (Bad Request)} if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PostMapping("/student/{id_student}/course/{id_course}")
    public ResponseEntity<CourseEnroll> enrollStudentInCourse(@PathVariable("id_student") Long idStudent,@PathVariable("id_course") Long idCourse) throws  URISyntaxException{

        return courseEnrollService.enrollStudentInCourse(idStudent,idCourse)
                .map(course -> new ResponseEntity<>(course, HttpStatus.OK))
                .orElseThrow(() -> new CourseEnrollException("couldn't enroll student in course"));
    }


    @GetMapping("/student-in-course/{id}")
    public ResponseEntity<List<Student>> getAllStudentsInCourse(@PathVariable Long id)  {
        return courseEnrollService.getAllStudentsInCourse(id)
                .map(students -> new ResponseEntity<>(students, HttpStatus.OK))
                .orElseThrow(() -> new CourseException("Course with ID " + id + " not found"));
    }


    @PostMapping("/student-in-courses")
    public ResponseEntity<List<Student>> getAllStudentsInCourses(@RequestBody  List<Course> courses)  {
        return courseEnrollService.getAllStudentsInCourses(courses)
                .map(students -> new ResponseEntity<>(students, HttpStatus.OK))
                .orElseThrow(() -> new CourseException("couldn't load student in courses"));
    }


    @PutMapping("/update-student-courses/{id}")
    public ResponseEntity<List<CourseEnroll>> getAllStudentsInCourses(@RequestBody  List<Course> courses,@PathVariable("id") Long idStudent)  {
        return courseEnrollService.updateStudentCourses(courses,idStudent)
                .map(courseEnrollList -> new ResponseEntity<>(courseEnrollList, HttpStatus.OK))
                .orElseThrow(() -> new CourseException("couldn't load student in courses"));
    }

    /**
     * {@code POST  /register} : Register new Student.
     *
     * @param student the student to register.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new course, or with status {@code 400 (Bad Request)} if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/register")
    public ResponseEntity<Student> registerUser(@RequestBody Student student)  throws  URISyntaxException {
        Student registredStdent = studentService.registerOne(student);
        return new ResponseEntity<>(registredStdent, HttpStatus.CREATED);
    }

    // Exception handling for InvalidInputException
    @ExceptionHandler(StudentException.class)
    public ResponseEntity<String> handleInvalidInputException(StudentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
