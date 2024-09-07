package mks.myworkspace.learna.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "learna_jobrole", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class JobRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Business fields here

	@CreationTimestamp
	@Column(name = "created_dte")
	Date createdDate;

	@Column(name = "modified_dte")
	@UpdateTimestamp
	Date modifiedDate;
}
