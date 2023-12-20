package com.issa.education.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "ed_course")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Where(clause = "is_deleted_=0")
@SQLDelete(sql = "update ed_course set is_deleted_=1 where id_ =? ")
@ToString
public class Course  extends AbstractAuditingEntity<Long>  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_")
    private Long id;

    @Column(name = "code_")
    private String code;

    @Column(name = "label_")
    @NotBlank
    private String label;








    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<CourseEnroll> courseEnrollList;

}
