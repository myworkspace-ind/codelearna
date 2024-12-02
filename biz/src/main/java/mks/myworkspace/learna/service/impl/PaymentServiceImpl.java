package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Order;
import mks.myworkspace.learna.entity.UserLibraryCourse;
import mks.myworkspace.learna.entity.Wallet;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.WalletRepository;
import mks.myworkspace.learna.service.OrderService;
import mks.myworkspace.learna.service.PaymentService;
import mks.myworkspace.learna.service.UserLibraryCourseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private WalletRepository walletRepository;
    
    @Autowired
    private CourseRepository courseRepository; // Thêm CourseRepository

    @Autowired
    private UserLibraryCourseService userLibraryCourseService;
    
    @Autowired
    private OrderService orderService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${payment.sepay.apiKey}")
    private String bearerToken;

    @Override
    public Double getBalance(String userEid) {
        Wallet wallet = walletRepository.findByUserEid(userEid);
        
        // Nếu không tìm thấy wallet, tạo mới
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUserEid(userEid);
            wallet.setBalance(0.0); // Khởi tạo số dư là 0
            walletRepository.save(wallet); // Lưu wallet mới vào cơ sở dữ liệu
        }
        
        return wallet.getBalance();
    }
    
    @Override
    public boolean payForCourse(String userEid, Long courseId) {
        Wallet wallet = walletRepository.findByUserEid(userEid);
        Course course = courseRepository.findById(courseId).orElse(null);

        // Kiểm tra xem wallet và course có tồn tại
        if (wallet == null || course == null) {
            return false; // Nếu không có wallet hoặc course, trả về false
        }

        // Kiểm tra xem khóa học đã tồn tại trong thư viện của người dùng chưa
        UserLibraryCourse existingCourse = userLibraryCourseService.getUserLibraryCourseById(courseId);
        if (existingCourse != null) {
            // Khóa học đã được mua trước đó
            return false; // Không thực hiện thanh toán
        }

        // Kiểm tra số dư có đủ không
        if (wallet.getBalance() >= course.getDiscountedPrice()) {
            // Trừ số dư
            wallet.setBalance(wallet.getBalance() - course.getDiscountedPrice());
            walletRepository.save(wallet); // Lưu wallet mới vào cơ sở dữ liệu

            // Thêm khóa học vào thư viện bằng phương thức đã có
            userLibraryCourseService.addCourseToLibrary(userEid, courseId, UserLibraryCourse.PaymentStatus.PURCHASED, UserLibraryCourse.ProgressStatus.IN_PROGRESS);

            return true; // Thanh toán thành công
        }

        // Nếu không đủ điều kiện, không làm gì cả và trả về false
        return false; // Thanh toán không thành công
    }

    public String processPayment(String orderCode, String userEid) {
        String url = "https://my.sepay.vn/userapi/transactions/list?limit=20";
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + bearerToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> response = responseEntity.getBody();

            List<Map<String, Object>> transactions = (List<Map<String, Object>>) response.get("transactions");

            if (transactions != null && !transactions.isEmpty()) {
                for (Map<String, Object> transaction : transactions) {
                    String code = (String) transaction.get("code");

                    if (code != null && code.equals(orderCode)) {
                        Optional<Order> order = orderService.getOrder(code);
                        BigDecimal transactionAmount = new BigDecimal((String) transaction.get("amount_in"));
                        if (order.isPresent() && order.get().getAmount().compareTo(transactionAmount) == 0) {
                            orderService.updateOrderStatus(order.get().getOrderCode(), Order.OrderStatus.COMPLETED);
                            userLibraryCourseService.addCourseToLibrary(userEid, order.get().getCourseId(), UserLibraryCourse.PaymentStatus.PURCHASED, UserLibraryCourse.ProgressStatus.IN_PROGRESS);
                            return "PAID";
                        }
                    }
                }
            }

            return orderCode + " not found";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred: " + e.getMessage());
        }
    }
}
