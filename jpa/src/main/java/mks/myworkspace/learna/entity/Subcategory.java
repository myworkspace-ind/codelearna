package mks.myworkspace.learna.entity;

import java.util.List;
import java.util.Set;
import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "learna_subcategory")
@Data
public class Subcategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "parameter_id", nullable = false)
    private Parameter parameter;

	@ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "subcategory", fetch = FetchType.EAGER)
    private List<Course> courses;
}