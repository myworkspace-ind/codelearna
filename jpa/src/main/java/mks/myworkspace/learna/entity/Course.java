package mks.myworkspace.learna.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
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

    @ManyToOne
    @JoinColumn(name = "difficulty_level_id", nullable = false)
    private Parameter difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "lesson_type_id", nullable = false)
    private Parameter lessonType;
    
    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(name = "is_free")
    private Boolean isFree ;
    
    @CreationTimestamp
    @Column(name = "created_dte")                                            
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dte")
    private Date modifiedDate;

    @ManyToMany
    @JoinTable(
        name = "course_lesson",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<Lesson> lessons;
    
    private Double averageRating;

}

