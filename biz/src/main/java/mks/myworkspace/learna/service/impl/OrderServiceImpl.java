package mks.myworkspace.learna.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Order;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.OrderJdbcRepository;
import mks.myworkspace.learna.repository.OrderRepository;
import mks.myworkspace.learna.service.OrderService;
import mks.myworkspace.learna.service.UserLibraryCourseService;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderRepository orderRepository;

    @Autowired
    private UserLibraryCourseService userLibraryCourseService;

    @Autowired
    private CourseRepository courseRepository; 
    
    @Autowired
    private OrderJdbcRepository orderJdbcRepository;
    
    @Value("${payment.sepay.generateQrEndpoint}")
    private String generateQrEndpoint;
    
    @Value("${payment.sepay.accountNumber}")
    private String accountNumber;
    
    @Value("${payment.sepay.bankCode}")
    private String bankCode;

    public String generateQrCodeUrl(String orderCode, BigDecimal amount) {
        String template = "qronly";
        boolean download = false;

        return String.format(
                "%s/img?acc=%s&bank=%s&amount=%s&des=%s&template=%s&download=%s",
                generateQrEndpoint,
                accountNumber,
                bankCode,
                amount,
                orderCode,
                template,
                download
        );
    }

    @Transactional
    public Order createOrder(String paymentMethod, String userEid, Long courseId) {
	    try {
	        if (paymentMethod == null || paymentMethod.isEmpty()) {
	            throw new RuntimeException("Payment method must not be null or empty");
	        }
	        if (userEid == null) {
	            throw new RuntimeException("User ID must not be null");
	        }
//	        log.info("userEid::::" + userEid);
//	        log.info("paymentMethod::::" + paymentMethod);
//	        log.info("courseId::::" + courseId);
	        
	        Course course = courseRepository.findById(courseId).orElse(null);
	        if (course == null) {
	            throw new RuntimeException("Course not found with ID: " + courseId);
	        }
	        
	        BigDecimal amount = new BigDecimal(course.getDiscountedPrice().toString());
	        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
	            throw new RuntimeException("Amount must be greater than zero");
	        }
	
	        boolean isExistingCourse = userLibraryCourseService.isCoursePurchased(userEid, courseId);
	        if (isExistingCourse) {
	            throw new RuntimeException("Course already purchased!"); 
	        }
	
	        Order order = Order.builder()
	                .paymentMethod(paymentMethod)
	                .amount(amount)
	                .userEid(userEid)
	                .courseId(courseId)
	                .orderCode(generateOrderCode())
	                .build();
	
	        Order savedOrder2 = orderJdbcRepository.save(order);
	        // orderRepository.flush();
	    	// log.error("Order successfully persisted with ID: " + savedOrder2.getOrderCode());
	
	        return order;
	    } catch (Exception e) {
	        log.error("Error occurred while creating order: " + e.getMessage(), e);
	        throw new RuntimeException("Failed to create order: " + e.getMessage());
	    }
    }

    public String generateOrderCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String currentDate = dateFormat.format(new Date());
        Random random = new Random();
        int randomNumber = random.nextInt(90000000) + 10000000;
        return "LA" + currentDate + randomNumber;
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
    
    public List<Order> getOrdersByUserEid(String userEid) {
        return orderRepository.findByUserEid(userEid);
    }
}
