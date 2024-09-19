package mks.myworkspace.learna.entity;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;


@Entity
@Table(name="learna_course_lesson", uniqueConstraints =@UniqueConstraint(columnNames = "id"))
@Data
public class Course_lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @CreationTimestamp
    @Column(name = "created_dte")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dte")
    private Date modifiedDate;
}
