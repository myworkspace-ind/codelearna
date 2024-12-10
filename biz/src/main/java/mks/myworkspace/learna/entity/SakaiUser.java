package mks.myworkspace.learna.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "sakai_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SakaiUser {
    @Id
    @Column(name = "USER_ID", length = 99)
    private String userId;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "EMAIL_LC", nullable = false)
    private String emailLc;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "TYPE")
    private String type = "user"; // default

    @Column(name = "PW", nullable = false)
    private String password ;

    @Column(name = "CREATEDBY")
    private String createdBy = "admin";

    @Column(name = "MODIFIEDBY")
    private String modifiedBy ="admin";

    @CreationTimestamp
    @Column(name = "CREATEDON", updatable = false)
    private Date createdOn;

    @UpdateTimestamp
    @Column(name = "MODIFIEDON")
    private Date modifiedOn;

    // Additional fields
    @Column(name = "id")
    private Long id =(long) 1;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "fullName")
    private String fullName="huy";

    @Column(name = "modified_date")
    private Date modifiedDate;
    
    @Column(name = "password")
    private String pw ="";
    
    @Column(name = "phone")
    private String phone;

    @Column(name = "role")
    private String role = "ROLE_USER"; // default
}

