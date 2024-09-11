package mks.myworkspace.learna.entity;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "learna_comment")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
    
//    @ManyToOne()
    
    @CreationTimestamp
    @Column(name = "created_dte")
    private Date createdDate;
    
    @UpdateTimestamp
    @Column(name = "modified_dte")
    private Date modifiedDate;
}
