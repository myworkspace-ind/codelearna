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

	@Override
	public String generatePaymentUrlVnpay(BigDecimal amount, String orderCode, String urlReturn, String ipAddress) {
		String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String vnp_Returnurl1 = "/codelearna-web/payment/vnpay-payment-return";
        String vnp_TmnCode1 = "MIV49RV3"; // kiểm tra email sau
        String vnp_HashSecret = "ZUPVJVQA7CL4FN1WMO5T7L4QEYOTES21"; // khi đăng ký Test
        String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = orderCode;
        String vnp_TmnCode = vnp_TmnCode1 ;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        
        BigDecimal vnpAmount = amount.multiply(new BigDecimal(100));
        vnp_Params.put("vnp_Amount", vnpAmount.toBigInteger().toString());

        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderCode);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

        urlReturn += vnp_Returnurl1;
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", ipAddress); 

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName).append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
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
        String queryUrl = query.toString();
        String salt = vnp_HashSecret;
        String vnp_SecureHash = hmacSHA512(salt, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
	}
	
	public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }
    
	 public String hashAllFields(Map fields) {
		 String vnp_HashSecret = "ZUPVJVQA7CL4FN1WMO5T7L4QEYOTES21";
	        List fieldNames = new ArrayList(fields.keySet());
	        Collections.sort(fieldNames);
	        StringBuilder sb = new StringBuilder();
	        Iterator itr = fieldNames.iterator();
	        while (itr.hasNext()) {
	            String fieldName = (String) itr.next();
	            String fieldValue = (String) fields.get(fieldName);
	            if ((fieldValue != null) && (fieldValue.length() > 0)) {
	                sb.append(fieldName);
	                sb.append("=");
	                sb.append(fieldValue);
	            }
	            if (itr.hasNext()) {
	                sb.append("&");
	            }
	        }
	        return hmacSHA512(vnp_HashSecret,sb.toString());
	}
	 
	@Override
	public int processReturnVnpay(Map<String, String> fields, String userEid) {
		String vnp_SecureHash = fields.get("vnp_SecureHash");
		 // Loại mã băm sử dụng SHA256, HmacSHA512
	    if (fields.containsKey("vnp_SecureHashType")) {
	        fields.remove("vnp_SecureHashType");
	    }
	    if (fields.containsKey("vnp_SecureHash")) {
	        fields.remove("vnp_SecureHash");
	    }
	    
	    String orderCode = fields.get("vnp_OrderInfo");
	    Optional<Order> order = orderService.getOrder(orderCode);
	    
	    String signValue = hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(fields.get("vnp_TransactionStatus"))) {
            	//cập nhật order, addCourseToLibrary
                orderService.updateOrderStatus(order.get().getOrderCode(), Order.OrderStatus.COMPLETED);
                userLibraryCourseService.addCourseToLibrary(userEid, order.get().getCourseId(), UserLibraryCourse.PaymentStatus.PURCHASED, UserLibraryCourse.ProgressStatus.IN_PROGRESS);
                return 1;
            } else {
                return 0;
            }
        }
        return -1;
	}   
}
