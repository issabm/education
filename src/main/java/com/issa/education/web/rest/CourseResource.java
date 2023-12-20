package com.issa.education.web.rest;

import com.issa.education.domain.Course;
import com.issa.education.service.CourseService;
import com.issa.education.web.rest.errors.CourseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseResource {

    @Autowired
    private  CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAll();
    }

    /**
     * {@code GET  /courses} : get all the courses.
     *
     * @param id for course id.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id)  {
        return courseService.getOneById(id)
                .map(course -> new ResponseEntity<>(course, HttpStatus.OK))
                .orElseThrow(() -> new CourseException("Course with ID " + id + " not found"));
    }



    /**
     * {@code POST  /courses} : Create a new course.
     *
     * @param course the course to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new course, or with status {@code 400 (Bad Request)} if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody Course course)  throws URISyntaxException {
        Course addedCourse = courseService.addOne(course);
        return new ResponseEntity<>(addedCourse, HttpStatus.CREATED);
    }



    /**
     * {@code PUT  /courses/:id} : Updates an existing course.
     *
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping
    public ResponseEntity<Course> update(@RequestBody Course course)   throws URISyntaxException {
        return courseService.update(course)
                .map(courseDTO -> new ResponseEntity<>(course, HttpStatus.OK))
                .orElseThrow(() -> new CourseException("couldn't update course with id " + course.getId() ));
    }


    /**
     * {@code PATCH  /courses/:id} : Partial updates given fields of an existing course, field will ignore if it is null
     *
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping
    public ResponseEntity<Course> partialUpdate(@RequestBody Course course)  throws URISyntaxException  {
        return courseService.partialUpdate(course)
                .map(courseDTO -> new ResponseEntity<>(course, HttpStatus.OK))
                .orElseThrow(() -> new CourseException("couldn't update course with id " + course.getId() ));
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param id the id of the course to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id)   throws URISyntaxException {
        courseService.deleteOne(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
