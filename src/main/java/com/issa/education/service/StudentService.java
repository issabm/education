package com.issa.education.service;

import com.issa.education.domain.Course;
import com.issa.education.domain.Student;
import com.issa.education.repository.CourseEnrollRepository;
import com.issa.education.repository.StudentRepository;
import com.issa.education.security.SecurityUtils;
import com.issa.education.web.rest.errors.StudentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Student}.
 */
@Service
public class StudentService {

    private final Logger log= LoggerFactory.getLogger(StudentService.class);



    private final CourseEnrollRepository courseEnrollRepository;

    private  final  StudentRepository studentRepository;
    @Autowired
    public StudentService(CourseEnrollRepository courseEnrollRepository,StudentRepository studentRepository) {
        this.courseEnrollRepository = courseEnrollRepository;
        this.studentRepository=studentRepository;
    }






   public void deleteOne(Long id){
        try {
            Optional<Student> student=this.getOneById(id);

            if (student.isEmpty()){
                throw new StudentException("Student with id "+ id + "not found");
            }

            List< Course> courses= courseEnrollRepository.getCourseEnrolledByStudent(id);

            if (!courses.isEmpty()){
                throw new StudentException("Student "+ student.get().getFullNameEn() + " already enrolled in " + courses.size() + " courses ");

            }

            studentRepository.delete(student.get());

        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }


    @Transactional(readOnly = true)
    public Optional<Student> getOneById(Long id) {
        try {
            log.debug("trying to get course by id  {}  ",id);
            return studentRepository.findById(id);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public Student registerOne(Student student){
        try {

            validateEmail(student.getEmail());
            validatePhone(student.getPhone());
            student.setCreatedDate(Instant.now());
            student.setIsDeleted(false);
            student.setCreatedBy(SecurityUtils.getCurrentUserLogin().orElse("SYSTEM"));
            student=   studentRepository.save(student);

        }
        catch (DataIntegrityViolationException e){

            throw new StudentException("Email or Tel already in use");
        }
        return student;
    }


    private void validateEmail(String email) {
        if (!email.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-z]{2,})$")) {
            throw new StudentException("Invalid email format");
        }
    }

    private void validatePhone(String phone) {
        if (!phone.matches("^\\+(?:[0-9] ?){6,14}[0-9]$")) {
            throw new StudentException("Invalid phone number format");
        }
    }
}
