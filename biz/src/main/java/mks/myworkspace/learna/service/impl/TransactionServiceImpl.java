package mks.myworkspace.learna.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.learna.entity.Transaction;
import mks.myworkspace.learna.repository.TransactionRepository;
import mks.myworkspace.learna.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository; 

    /**
     * Lưu giao dịch nếu chưa tồn tại
     * @param transactionData Dữ liệu giao dịch từ API
     */
    public void saveTransaction(Map<String, Object> transactionData) {
        String referenceNumber = (String) transactionData.get("reference_number");

        Optional<Transaction> existingTransaction = transactionRepository.findByReferenceNumber(referenceNumber);
        if (existingTransaction.isPresent()) {
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Transaction transaction = Transaction.builder()
                .referenceNumber(referenceNumber)
                .bankBrandName((String) transactionData.get("bank_brand_name"))
                .accountNumber((String) transactionData.get("account_number"))
                .transactionDate(LocalDateTime.parse((String) transactionData.get("transaction_date"), formatter))
                .amountIn(new BigDecimal((String) transactionData.get("amount_in")))
                .transactionContent((String) transactionData.get("transaction_content"))
                .code((String) transactionData.get("code"))
                .bankAccountId((String) transactionData.get("bank_account_id"))
                .build();

        transactionRepository.save(transaction);
    }
}
