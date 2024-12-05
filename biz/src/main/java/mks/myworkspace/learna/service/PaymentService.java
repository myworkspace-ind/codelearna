package mks.myworkspace.learna.service;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentService {
    Double getBalance(String userEid);
    boolean payForCourse(String userEid, Long courseId);
    public String processPayment(String orderCode, String userEid);
    String generatePaymentUrlVnpay(BigDecimal amount, String orderCode, String urlReturn, String ipAddress);
    public int processReturnVnpay(Map<String, String> fields, String userEid);
    public String hashAllFields(Map fields);
    public String hmacSHA512(final String key, final String data);
}
