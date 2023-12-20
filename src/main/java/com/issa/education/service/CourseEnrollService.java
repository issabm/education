package com.issa.education.service;

import com.issa.education.domain.Course;
import com.issa.education.domain.CourseEnroll;
import com.issa.education.domain.Student;
import com.issa.education.repository.CourseEnrollRepository;
import com.issa.education.security.SecurityUtils;
import com.issa.education.web.rest.errors.CourseEnrollException;
import com.issa.education.web.rest.errors.CourseException;
import com.issa.education.web.rest.errors.StudentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CourseEnroll}.
 */
@Service
public class CourseEnrollService {


    private final Logger log= LoggerFactory.getLogger(CourseEnrollService.class);

    private final StudentService studentService;
    private final CourseService courseService;
    private  final CourseEnrollRepository courseEnrollRepository;
    @Autowired
    public CourseEnrollService(StudentService studentService, CourseService courseService,CourseEnrollRepository courseEnrollRepository) {
        this.courseEnrollRepository = courseEnrollRepository;
        this.studentService=studentService;
        this.courseService=courseService;
    }



    public Optional<List<Student>> getAllStudentsInCourse(Long idCourse) {
        try {
            Optional<Course> course=courseService.getOneById(idCourse);
            if (course.isEmpty()){
                throw new CourseException("course with id "+ idCourse +" not found");
            }
            return Optional.of(courseEnrollRepository.findAllStudentsEnrolledInCourseByIdCourse(idCourse));
        }
        catch (Exception e){

            log.error(e.getMessage());
        }
        throw new CourseEnrollException("couldn't load students in course " + idCourse);
    }

    public Optional<List<Student>> getAllStudentsInCourses(List<Course> courses) {
        try {

          return Optional.of(   courses.stream().map(Course::getId).map(courseEnrollRepository::findAllStudentsEnrolledInCourseByIdCourse)
                    .flatMap(List::stream)
                    .collect(Collectors.toList()));

        }
        catch (Exception e){

            log.error(e.getMessage());
        }
        throw new CourseEnrollException("couldn't load students in courses " );
    }


    public Optional<List<CourseEnroll>> updateStudentCourses(List<Course> courses, Long idStudent) {
        try {

            Optional<Student> student=studentService.getOneById(idStudent);
            if (student.isEmpty()){
                throw new StudentException("student with id "+ idStudent +" not found");
            }
            List<CourseEnroll> existingCourseEnrollList=courseEnrollRepository.getCourseEnrollByStudent(idStudent);

            if (!courses.isEmpty()){
                courseEnrollRepository.deleteAll(existingCourseEnrollList);
            }
            List<Optional<CourseEnroll>> enrollments = courses.stream()
                    .map(course -> enrollStudentInCourse(course.getId(), idStudent))
                    .collect(Collectors.toList());

            // Check if any enrollment failed (i.e., returned an empty Optional)
            if (enrollments.stream().anyMatch(Optional::isEmpty)) {
                return Optional.empty();
            }

            // Extract values from Optional<CourseEnroll> and collect into List<CourseEnroll>
            List<CourseEnroll> courseEnrollList = enrollments.stream()
                    .map(Optional::get)
                    .collect(Collectors.toList());

            return Optional.of(courseEnrollList);



        }
        catch (Exception e){

            log.error(e.getMessage());
        }
        throw new CourseEnrollException("couldn't load students in courses " );
    }

    public Optional<CourseEnroll> enrollStudentInCourse(Long idStudent, Long idCourse){

        try {

            Optional<Student> student=studentService.getOneById(idStudent);
            Optional<Course> course=courseService.getOneById(idCourse);

           if (student.isEmpty()){
               throw new StudentException("student with id "+ idStudent +" not found");
           }
            if (course.isEmpty()){
                throw new CourseException("course with id "+ idCourse +" not found");
            }

            if (courseEnrollRepository.countAllByStudentAndCourse(student.get(),course.get())>0){
                throw new CourseEnrollException("student "+ student.get().getFullNameEn() + " already enroll in course " + course.get().getLabel());

            }

            CourseEnroll courseEnroll=CourseEnroll
                    .builder()
                    .student(student.get())
                    .course(course.get())
                    .build();

            courseEnroll.setCreatedBy(SecurityUtils.getCurrentUserLogin().orElse("SYSTEM"));
            courseEnroll.setCreatedDate(Instant.now());
            courseEnroll.setIsDeleted(false);
            return Optional.of(courseEnrollRepository.save(courseEnroll));

        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();

    }
}
