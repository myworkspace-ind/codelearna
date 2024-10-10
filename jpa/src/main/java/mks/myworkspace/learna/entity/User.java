package mks.myworkspace.learna.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "learna_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "balance", nullable = false)
    private Double balance = 0.0;

    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @Column(name = "role", nullable = false)
    private String role; // Ex: "student", "teacher", "admin"

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE"; // Ex: "ACTIVE", "LOCKED"

    @Column(name = "last_login_date")
    private Date lastLoginDate;

    @Column(name = "verified", nullable = false)
    private Boolean verified = false;
    
    @CreationTimestamp
    @Column(name = "created_dte")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dte")
    private Date modifiedDate;
    
    //ref reviews
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
