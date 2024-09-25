package mks.myworkspace.learna.entity;

import javax.persistence.*;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "learna_user_library_course", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class UserLibraryCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "progress_status", nullable = false)
    private ProgressStatus progressStatus;

    @CreationTimestamp
    @Column(name = "created_dte")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dte")
    private Date modifiedDate;

    public enum PaymentStatus {
        PURCHASED,
        TRIAL
    }

    public enum ProgressStatus {
        IN_PROGRESS,
        COMPLETE
    }
}