package mks.myworkspace.learna.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "learna_category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String name;

    // Thay thế name bằng tham chiếu tới Parameter
    @ManyToOne
    @JoinColumn(name = "parameter_id", nullable = true)
    private Parameter parameter;
    
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Subcategory> subcategories; 
}