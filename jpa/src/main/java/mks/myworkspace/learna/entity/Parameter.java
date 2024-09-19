package mks.myworkspace.learna.entity;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "learna_parameter")

public class Parameter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "param_key", unique = true, nullable = false)
	private String paramKey;
	
	@Column(name = "param_value")
	private String paramValue;
	
	@Column(name = "description")
	private String description;
	
	
}
