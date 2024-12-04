package mks.myworkspace.learna.entity;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "learna_parameter", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "param_key", "param_value" }) })

public class Parameter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "param_key", nullable = false)
	private String paramKey;

	@Column(name = "param_value", nullable = false)
	private String paramValue;

	
	@Column(name = "seqno")
	private Integer seqno;
	

	@Column(name = "description")
	private String description;

}
