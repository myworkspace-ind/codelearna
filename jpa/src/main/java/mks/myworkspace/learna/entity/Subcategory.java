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
@Table(name = "learna_subcategory")
@Data
public class Subcategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "parameter_id", nullable = true)
    private Parameter parameter;

	@ManyToOne
    @JoinColumn(name = "category_id")
	@ToString.Exclude
    private Category category;

    @OneToMany(mappedBy = "subcategory", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Course> courses;
}