package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.Order;
import mks.myworkspace.learna.service.OrderService;
import mks.myworkspace.learna.service.PaymentService;
import mks.myworkspace.learna.service.UserLibraryCourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/payment")
public class PaymentController extends BaseController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserLibraryCourseService userLibraryCourseService;
    @Autowired
    private OrderService orderService;
    
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/balance")
    public ModelAndView getBalance(HttpServletRequest request, HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("balanceView"); // View name to display balance
        String userEid = getCurrentUserEid();
        Double balance = paymentService.getBalance(userEid);
        mav.addObject("balance", balance);
        return mav;
    }

    @PostMapping("/pay")
    public ModelAndView payForCourse(@RequestParam Long courseId, HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/library");
        try {
            String userEid = getCurrentUserEid();
            boolean success = paymentService.payForCourse(userEid, courseId);
            if (success) {
                // Cập nhật số dư và thông báo
                Double newBalance = paymentService.getBalance(userEid);
                httpSession.setAttribute("userBalance", newBalance);
                httpSession.setAttribute("paymentMessage", "Payment successful and the course has been added to your library.");
                httpSession.setAttribute("alertType", "success");
            } else {
                httpSession.setAttribute("paymentMessage", "Payment failed. Please check later.");
                httpSession.setAttribute("alertType", "danger");
            }
        } catch (Exception e) {
            httpSession.setAttribute("paymentMessage", "Error occurred while processing the payment.");
            httpSession.setAttribute("alertType", "danger");
        }
        return mav;
    }
    
    @GetMapping("/check/{orderCode}")
    public ResponseEntity<String> processPayment(@PathVariable String orderCode) {
        try {
            String userEid = getCurrentUserEid();
            String result = paymentService.processPayment(orderCode, userEid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
    
    //xử lý thanh toán 
    @PostMapping("/vnpay")
    public String processPaymentWithVnpay(HttpServletRequest request) {
    	
        Order order = (Order) request.getAttribute("order");
        if(order == null) {
            throw new RuntimeException("Order is missing");
        }
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String ipAddress = request.getRemoteAddr();
        String vnpayUrl = paymentService.generatePaymentUrlVnpay(order.getAmount(), order.getOrderCode(), baseUrl, ipAddress);
        log.info("Generated VNPay URL: {}", vnpayUrl);
        return "redirect: " + vnpayUrl;
    }
    
    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String paymentCompletedVnpay(HttpServletRequest request,HttpSession httpSession, Model model){
    	
    	initSession(request, httpSession);
    	String userEid = getCurrentUserEid();
    	
    	log.warn(userEid+ "-------");
    	Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }
        
        int paymentStatus = paymentService.processReturnVnpay(fields, userEid);

	    return "redirect: /codelearna-web";

    }
    
}