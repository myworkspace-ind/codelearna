package mks.myworkspace.learna.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "learna_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String phone;

    @Column(name = "role", nullable = false)
    private String role = "ROLE_USER"; // Mặc định là ROLE_USER
//    
//    @OneToOne(mappedBy = "user")
//    private Organization organization;
//
//    
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<CV> cvList;
    
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private Date createdDate;
    
    @UpdateTimestamp
    @Column(name = "modified_date")
    private Date modifiedDate;
}