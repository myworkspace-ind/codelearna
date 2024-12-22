package mks.myworkspace.learna.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table (name = "learna_transaction")
public class Transaction {
    @Id
    private String referenceNumber;

    private String bankBrandName;
    private String accountNumber;
    private LocalDateTime transactionDate;
    private BigDecimal amountIn;
    private String transactionContent;
    private String code;
    private String bankAccountId;
}