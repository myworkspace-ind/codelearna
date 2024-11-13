package mks.myworkspace.learna.service;

import java.math.BigDecimal;

public interface PaymentService {
    Double getBalance(String userEid);
    boolean payForCourse(String userEid, Long courseId);
}
