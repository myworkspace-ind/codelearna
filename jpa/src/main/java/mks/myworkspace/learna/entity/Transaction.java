package mks.myworkspace.learna.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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