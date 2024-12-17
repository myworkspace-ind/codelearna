package mks.myworkspace.learna.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "learna_business", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Data
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "logo_url")
    private String logoUrl;

    @Temporal(TemporalType.DATE)
    @Column(name = "establishment_date")
    private Date establishmentDate;

    @CreationTimestamp
    @Column(name = "created_dte")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dte")
    private Date modifiedDate;

    

    // Thêm các trường và mối quan hệ khác nếu cần
}