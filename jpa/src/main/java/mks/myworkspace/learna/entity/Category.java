package mks.myworkspace.learna.entity;

import java.util.List;
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

    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Subcategory> subcategories; 
}