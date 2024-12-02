package mks.myworkspace.learna.entity;

import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "learna_orders")
public class Order {
    @Id
    @Column(name = "order_code", unique = true, nullable = false)
    private String orderCode;

    @Column(name = "user_eid", nullable = false)
    private String userEid;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void setDefaultStatus() {
        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public enum OrderStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }
}