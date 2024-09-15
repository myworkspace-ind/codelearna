package mks.myworkspace.learna.entity;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Entity
@Table(name = "learna_course", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "original_price", nullable = false)
    private Double originalPrice;

    @Column(name = "discounted_price", nullable = false)
    private Double discountedPrice;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "difficulty_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @Column(name = "lesson_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LessonType lessonType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ElementCollection
    @MapKeyColumn(name = "star")
    @Column(name = "rating_count")
    private Map<Integer, Integer> ratings; 

    @CreationTimestamp
    @Column(name = "created_dte")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dte")
    private Date modifiedDate;

    public enum DifficultyLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT,
        MASTER
    }

    public enum LessonType {
        VIDEO,
        INTERACTIVE
    }
}

