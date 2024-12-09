package mks.myworkspace.learna.entity;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

import lombok.*;


@Entity
@Table(name = "learna_lesson_tracking", uniqueConstraints = @UniqueConstraint(columnNames = {"user_eid", "course_id", "lesson_id"}))
@Data
public class LessonTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_eid", nullable = false)
    private String userEid;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @Column(name = "course_url", nullable = false)
    private String courseUrl;

    @Column(name = "activity_id", nullable = false)
    private String activityId;
    
    @CreationTimestamp
    @Column(name = "created_dte")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dte")
    private Date modifiedDate;

}
