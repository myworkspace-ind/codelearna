package mks.myworkspace.learna.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import lombok.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.learna.entity.Order;
import mks.myworkspace.learna.repository.OrderRepository;
import mks.myworkspace.learna.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderRepository orderRepository;

    public String generateQrCodeUrl(String orderCode, BigDecimal amount) {
        String accountNumber = "014028268888";
        String bankCode = "MB";
        String template = "compact";
        boolean download = false;

        return String.format(
                "https://qr.sepay.vn/img?acc=%s&bank=%s&amount=%s&des=%s&template=%s&download=%s",
                accountNumber,
                bankCode,
                amount,
                orderCode,
                template,
                download
        );
    }

    public Order createOrder(String paymentMethod, BigDecimal amount, Long userId, Long courseId) {
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new RuntimeException("Payment method must not be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }
        if (userId == null) {
            throw new RuntimeException("User ID must not be null");
        }

        Order order = Order.builder()
                .paymentMethod(paymentMethod)
                .amount(amount)
                .userId(userId)
                .courseId(courseId)
                .orderCode(generateOrderCode(userId))
                .build();

        orderRepository.save(order);
        return order;
    }


    public String generateOrderCode(Long userId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String currentDate = dateFormat.format(new Date());
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        return "CLA" + userId + currentDate + randomNumber;
    }


    public Optional<Order> getOrder(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    public boolean updateOrderStatus(String orderCode, Order.OrderStatus status) {
        Optional<Order> orderOptional = getOrder(orderCode);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status);
            orderRepository.save(order);
            return true;
        }

        return false;
    }
}
