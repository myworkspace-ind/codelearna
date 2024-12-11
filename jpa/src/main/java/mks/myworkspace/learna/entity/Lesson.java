package mks.myworkspace.learna.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "learna_lesson", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Data
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "video_url")
    private String videoUrl;
    
    @Column(name = "activity_id")
    private String activityId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

  
    @Column(name = "status", nullable = false)
    private String status;  


    @Column(name = "order_number")
    private Integer orderNumber;

    @CreationTimestamp
    @Column(name = "created_dte")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dte")
    private Date modifiedDate;

    public List<Comment> getComments() {
        return comments;
    }
    public enum status {
        ACTIVE,
        DELETED
    }
}
