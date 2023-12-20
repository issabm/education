package com.issa.education.domain;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ed_course_enroll")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Where(clause = "is_deleted_=0")
@SQLDelete(sql = "update ed_course_enroll set is_deleted_=1 where id_ =? ")
@ToString
public class CourseEnroll  extends AbstractAuditingEntity<Long>  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_")
    private Long id;


    // table relations


    @ManyToOne
    @JoinColumn(name = "id_student")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_course")
    private Course course;

}
