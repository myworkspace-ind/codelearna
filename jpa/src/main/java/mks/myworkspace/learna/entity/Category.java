package mks.myworkspace.learna.entity;

import java.util.Set;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "learna_category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> courses;
}