package com.issa.education.repository;

import com.issa.education.domain.Course;
import com.issa.education.domain.CourseEnroll;
import com.issa.education.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the CourseEnroll entity.
 */
@Repository
public interface CourseEnrollRepository extends JpaRepository<CourseEnroll,Long> {


  @Query("SELECT courseEnroll FROM  CourseEnroll courseEnroll WHERE  courseEnroll.student.id=?1")
  List<CourseEnroll> getCourseEnrollByStudent(Long idStudent);

  @Query("SELECT courseEnroll.course FROM  CourseEnroll courseEnroll WHERE  courseEnroll.student.id=?1")
  List<Course> getCourseEnrolledByStudent(Long idStudent);

  @Query("SELECT courseEnroll.student FROM CourseEnroll  courseEnroll where  courseEnroll.course.id=?1")
  List<Student> findAllStudentsEnrolledInCourseByIdCourse(Long id);
  int countAllByStudentAndCourse(Student student, Course course);

}
