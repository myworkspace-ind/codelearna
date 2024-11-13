package mks.myworkspace.learna.entity;

import javax.persistence.*;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "learna_user_wallet", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_eid", nullable = false)
    private String userEid;

    @Column(name = "balance", nullable = false)
    private Double balance;
    
    @CreationTimestamp
    @Column(name = "created_dte")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dte")
    private Date modifiedDate;
}