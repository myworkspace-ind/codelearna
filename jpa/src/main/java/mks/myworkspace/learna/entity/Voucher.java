package mks.myworkspace.learna.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "learna_voucher", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Data
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign; 

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "discount_value", nullable = false)
    private Double discountValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", nullable = false)
    private ValueType valueType;

    @Column(name = "max_value")
    private Double maxValue;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "`condition`", columnDefinition = "TEXT")
    private String condition;

    public enum ValueType {
        PERCENTAGE("percentage"),
        FIXED("fixed");

        private final String value;

        ValueType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        @JsonCreator
        public static ValueType fromValue(String value) {
            for (ValueType type : ValueType.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }
}
