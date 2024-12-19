package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Order;
import mks.myworkspace.learna.entity.UserLibraryCourse;
import mks.myworkspace.learna.entity.Wallet;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.WalletRepository;
import mks.myworkspace.learna.service.OrderService;
import mks.myworkspace.learna.service.PaymentService;
import mks.myworkspace.learna.service.TransactionService;
import mks.myworkspace.learna.service.UserLibraryCourseService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private WalletRepository walletRepository;
    
    @Autowired
    private CourseRepository courseRepository; // Thêm CourseRepository

    @Autowired
    private UserLibraryCourseService userLibraryCourseService;
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private OrderService orderService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${payment.sepay.apiKey}")
    private String bearerToken;
    
    @Value("${payment.sepay.transactionEndpoint}")
    private String transactionEndpoint;
    
    @Value("${payment.vnpay.tmnCode}")
    private String tmnCode;

    @Value("${payment.vnpay.secretKey}")
    private String secretKey;

    @Value("${payment.vnpay.url}")
    private String vnpayUrl;
    
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
        String url = transactionEndpoint;  
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + bearerToken);

            // Log thông tin chi tiết request
            // log.warn("Requesting transaction endpoint");
            // log.warn("Request URL: {}", url);
            // log.warn("Request Headers: {}", headers);

            // Thực hiện HTTP GET
            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            // Log thông tin response
            // log.warn("Response Status Code: {}", responseEntity.getStatusCode());
            // log.warn("Response Headers: {}", responseEntity.getHeaders());
            // log.warn("Response Body: {}", responseEntity.getBody());

            Map<String, Object> response = responseEntity.getBody();
            List<Map<String, Object>> transactions = (List<Map<String, Object>>) response.get("transactions");

            if (transactions != null && !transactions.isEmpty()) {
                log.info("Transactions found: {}", transactions);

                for (Map<String, Object> transaction : transactions) {
                    String code = (String) transaction.get("code");

                    if (code != null && code.equals(orderCode)) {
                        Optional<Order> order = orderService.getOrder(code);
                        BigDecimal transactionAmount = new BigDecimal((String) transaction.get("amount_in"));

                        if (order.isPresent() && order.get().getAmount().compareTo(transactionAmount) == 0) {
                            // Cập nhật trạng thái đơn hàng
                        	orderService.updateOrderStatusJdbc(order.get().getOrderCode(), Order.OrderStatus.COMPLETED);
                            //orderService.updateOrderStatus(order.get().getOrderCode(), Order.OrderStatus.COMPLETED);
//                            log.warn("Order Code:::::" + order.get().getOrderCode());
//                            log.warn("Sepay Code:::::" + code);
                            // Thêm khóa học vào thư viện người dùng
                            userLibraryCourseService.addCourseToLibrary(
                                userEid,
                                order.get().getCourseId(),
                                UserLibraryCourse.PaymentStatus.PURCHASED,
                                UserLibraryCourse.ProgressStatus.IN_PROGRESS
                            );

                            // Lưu thông tin giao dịch
                            transactionService.saveTransaction(transaction);

                            log.info("Payment successfully processed for order code: {}", orderCode);
                            return "PAID";
                        }
                    }
                }
            }

            log.warn("No matching transactions found for order code: {}", orderCode);
            return orderCode + " not found";

        } catch (HttpClientErrorException e) {
            // Xử lý lỗi HTTP cụ thể (401 Unauthorized)
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.error("Unauthorized access (401) when accessing the endpoint: {}", url);
                log.error("API Key::::", bearerToken);
                log.error("Response Body: {}", e.getResponseBodyAsString());
            } else {
                log.error("HTTP error occurred: {}", e.getStatusCode());
                log.error("Response Body: {}", e.getResponseBodyAsString());
            }
            throw new RuntimeException("An error occurred during payment processing: " + e.getMessage(), e);
        } 
    }



    //tao url thanh toan vnpay
	@Override
	public String generatePaymentUrlVnpay(BigDecimal amount, String orderCode, String urlReturn, String ipAddress) {
		String vnp_PayUrl = vnpayUrl;
        String vnp_HashSecret = secretKey; // Khóa bí mật (secret key) của VNPAY khi đăng ký dịch vụ thanh toán thử nghiệm
        String vnp_TmnCode = tmnCode; 

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");// Phiên bản của cổng thanh toán VNPAY
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);//    // Mã thiết bị (tmnCode) là mã máy chủ của đơn vị cung cấp dịch vụ
        
        BigDecimal vnpAmount = amount.multiply(new BigDecimal(100));
        vnp_Params.put("vnp_Amount", vnpAmount.toBigInteger().toString());// Thêm số tiền vào tham số

        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", orderCode); // Mã giao dịch (txnRef) là mã đơn hàng được truyền vào từ tham số
        vnp_Params.put("vnp_OrderInfo", orderCode);// Mô tả thông tin đơn hàng
        vnp_Params.put("vnp_OrderType", "order-type");
        vnp_Params.put("vnp_Locale", "vn");

        urlReturn += "/codelearna-web/payment/vnpay-payment-return";  
        vnp_Params.put("vnp_ReturnUrl", urlReturn);// URL trả về sau khi thanh toán
        vnp_Params.put("vnp_IpAddr", ipAddress); 

        // Lấy thời gian hiện tại và định dạng theo kiểu "yyyyMMddHHmmss"
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));// Sử dụng múi giờ Việt Nam
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate); // Thêm thời gian tạo vào tham số
        
        // Thêm thời gian hết hạn của giao dịch (15 phút kể từ thời điểm tạo)
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Tạo danh sách các tham số và sắp xếp chúng theo tên (bắt buộc phải sắp xếp)
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        
        // Chuẩn bị chuỗi để mã hóa (hashData) và query (queryUrl)
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        
        // Duyệt qua các tham số đã sắp xếp và xây dựng query string và chuỗi hash
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName).append('=');
                try {
                	// Mã hóa các giá trị tham số và thêm vào chuỗi hash
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    
                    // Tạo query string cho URL
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                            .append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        
        // Tạo URL truy vấn cho cổng thanh toán VNPAY
        String queryUrl = query.toString();
        
        // Tính toán chuỗi bảo mật bằng thuật toán HMAC-SHA512 với khóa bí mật
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        // Thêm giá trị bảo mật vào URL
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        
        String paymentUrl = vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
	}
	
	//Hàm tính toán mã băm (hash) bằng thuật toán HMAC-SHA512.
	@Override
	public String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            
            // Tạo một instance của HMAC-SHA512 từ thư viện Java Cryptography Architecture (JCA).
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            
            // Chuyển đổi khóa bí mật thành mảng byte.
            byte[] hmacKeyBytes = key.getBytes();
            
            // Tạo đối tượng SecretKeySpec để gán khóa bí mật với thuật toán HMAC-SHA512.
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            
            // Chuyển đổi dữ liệu đầu vào (data) thành mảng byte với mã hóa UTF-8.
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            
            // Tính toán HMAC-SHA512 và trả về kết quả dưới dạng mảng byte.
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff)); // "%02x" định dạng byte dưới dạng hex.
            }
            
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Xử lý phản hồi từ VNPay khi thanh toán hoàn tất
     * @param fields
     * @param userEid
     * @return
     */
	@Override
	public int processReturnVnpay(Map<String, String> fields, String userEid) {
		// Lấy giá trị chữ ký (secure hash) được trả về từ VNPay
		String vnp_SecureHash = fields.get("vnp_SecureHash");

		// Loại bỏ các trường không cần thiết khỏi Map để tính lại chữ ký
	    if (fields.containsKey("vnp_SecureHashType")) {
	        fields.remove("vnp_SecureHashType");
	    }

	    if (fields.containsKey("vnp_SecureHash")) {
	        fields.remove("vnp_SecureHash");
	    }
	    
	    //Tìm kiếm thông tin đơn hàng tương ứng
	    String orderCode = fields.get("vnp_OrderInfo");
	    Optional<Order> order = orderService.getOrder(orderCode);
	    
	    //So sánh chữ ký đã tính toán với chữ ký được trả về từ VNPay
	    String signValue = hashAllFields(fields);

	    //Nếu chữ ký khớp, kiểm tra trạng thái giao dịch từ trường "vnp_TransactionStatus".
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(fields.get("vnp_TransactionStatus"))) {
            	
            	// Giao dịch thành công: Cập nhật trạng thái đơn hàng và thêm khóa học 
                orderService.updateOrderStatus(order.get().getOrderCode(), Order.OrderStatus.COMPLETED);
                userLibraryCourseService.addCourseToLibrary(userEid, order.get().getCourseId(), UserLibraryCourse.PaymentStatus.PURCHASED, UserLibraryCourse.ProgressStatus.IN_PROGRESS);
                return 1;
            } else {
            	
            	// Trạng thái giao dịch không phải "00", giao dịch thất bại.
                return 0;
            }
        }
        
        return -1;
	}   
	
	// Tạo một chuỗi băm (hash) HMAC-SHA512 từ các trường trong map
	@Override
	public String hashAllFields(Map fields) {
		//Lấy danh sách tên các trường (keys) từ bản đồ và đưa vào danh sách.
	    List fieldNames = new ArrayList(fields.keySet());
	    Collections.sort(fieldNames);
	    
	    //Sử dụng StringBuilder để xây dựng chuỗi đầu vào cho hàm băm.
	    StringBuilder sb = new StringBuilder();
	    Iterator itr = fieldNames.iterator();
	    while (itr.hasNext()) {
	        String fieldName = (String) itr.next();
	        String fieldValue = (String) fields.get(fieldName);
	        
	        // - Chỉ thêm trường vào chuỗi nếu giá trị không null và không rỗng.
	        if ((fieldValue != null) && (fieldValue.length() > 0)) {
	            sb.append(fieldName);
	            sb.append("=");
	            sb.append(fieldValue);
	        }
	        
	        // Nếu còn trường tiếp theo, thêm ký tự "&" vào chuỗi.
	        if (itr.hasNext()) {
	            sb.append("&");
	        }
	    }
	    return hmacSHA512(secretKey,sb.toString());
	}
}
