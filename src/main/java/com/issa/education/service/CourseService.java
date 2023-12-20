package com.issa.education.service;


import com.issa.education.domain.Course;
import com.issa.education.repository.CourseRepository;
import com.issa.education.security.SecurityUtils;
import com.issa.education.web.rest.errors.CourseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing {@link Course}.
 */
@Service
public class CourseService {


    private final Logger log=LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<Course> getAll() {
        try {
            log.debug("trying getting all courses ");
            return courseRepository.findAll();
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        throw  new CourseException("Couldn't get all courses");

    }

    @Transactional(readOnly = true)
    public Optional<Course> getOneById(Long id) {
        try {
            log.debug("trying to get course by id  {}  ",id);
            return courseRepository.findById(id);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Course> update(Course course) {
        log.debug("Request to update course : {}", course);
       // course.setEditedBy(SecurityUtils.getCurrentUserLogin().orElse("SYSTEM"));
        return Optional.ofNullable(courseRepository
                .findById(course.getId())
                .map(existingCourse -> {
                    // Set the properties of the existing course
                    course.setLastModifiedBy(SecurityUtils.getCurrentUserLogin().orElse("SYSTEM"));
                    course.setLastModifiedDate(Instant.now());

                    return course;
                })
                .map(courseRepository::save)
                .orElseThrow(() -> new CourseException("Course with ID " + course.getId() + " not found")));

    }
    public Course addOne(Course course) {
        try {
            log.debug("trying to add course {}  ",course.toString());

            course.setIsDeleted(false);
            course.setCreatedBy(SecurityUtils.getCurrentUserLogin().orElse("SYSTEM"));
            course.setCreatedDate(Instant.now());
            return courseRepository.save(course);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
       throw  new CourseException("couldn't save course ");
    }

    public Optional<Course> partialUpdate(Course course) {
        log.debug("Request to partially update CategorieArticle : {}", course);

        return courseRepository
                .findById(course.getId())
                .map(existingCourse -> {
                   course.setLastModifiedBy(SecurityUtils.getCurrentUserLogin().orElse("SYSTEM"));
                    course.setLastModifiedDate(Instant.now());

                    if (course.getCode() != null) {
                        existingCourse.setCode(course.getCode());
                    }
                    if (course.getLabel() != null) {
                        existingCourse.setLabel(course.getLabel());
                    }
                    return existingCourse;
                })
                .map(courseRepository::save);

    }
    public void deleteOne(Long id) {
        try {
            log.debug("trying to delete course with id {}  ",id);
            courseRepository.deleteById(id);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }


}
