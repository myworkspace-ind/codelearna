package mks.myworkspace.learna.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import lombok.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Order;
import mks.myworkspace.learna.entity.UserLibraryCourse;
import mks.myworkspace.learna.repository.OrderRepository;
import mks.myworkspace.learna.service.OrderService;
import mks.myworkspace.learna.service.UserLibraryCourseService;
import mks.myworkspace.learna.repository.CourseRepository;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderRepository orderRepository;

    @Autowired
    private UserLibraryCourseService userLibraryCourseService;

    @Autowired
    private CourseRepository courseRepository; 

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

    public Order createOrder(String paymentMethod, String userEid, Long courseId) {
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new RuntimeException("Payment method must not be null or empty");
        }
        if (userEid == null) {
            throw new RuntimeException("User ID must not be null");
        }

        Course course = courseRepository.findById(courseId).orElse(null);
        BigDecimal amount = new BigDecimal(course.getDiscountedPrice().toString());
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        UserLibraryCourse existingCourse = userLibraryCourseService.getUserLibraryCourseById(courseId);
        if (existingCourse != null) {
            throw new RuntimeException("Course already purchased!"); 
        }

        Order order = Order.builder()
                .paymentMethod(paymentMethod)
                .amount(amount)
                .userEid(userEid)
                .courseId(courseId)
                .orderCode(generateOrderCode())
                .build();

        orderRepository.save(order);
        return order;
    }


    public String generateOrderCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String currentDate = dateFormat.format(new Date());
        Random random = new Random();
        int randomNumber = random.nextInt(90000000) + 10000000;
        return "CLA" + currentDate + randomNumber;
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
