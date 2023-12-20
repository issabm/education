package com.issa.education.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "ed_student",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "email_"},name = "UC_EMAIL"), @UniqueConstraint(columnNames = { "phone_"},name = "UK_PHONE") })
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Where(clause = "is_deleted_=0")
@SQLDelete(sql = "update ed_student set is_deleted_=1 where id_ =? ")
@ToString
public class Student  extends AbstractAuditingEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_")
    private Long id;


    @Column(name = "name_ar_")
    private String fullNameAr;

    @Column(name = "name_en_")
    private String fullNameEn;


    @Column(name = "email_",unique = true)
    @Email

    private String email;


    @Column(name = "phone_",unique = true)
    private String phone;

    @Column(name = "address_")
    @Lob
    private String address;

    @Column(name = "is_deleted_")
    private Boolean isDeleted;



    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<CourseEnroll> courseEnrollList;

}
