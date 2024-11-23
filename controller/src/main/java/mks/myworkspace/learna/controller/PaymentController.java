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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
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


    @Value("${payment.sepay.apiKey}")
    private String bearerToken;

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
                            return ResponseEntity.ok("PAID");
                        }
                    }
                }
            }

            return ResponseEntity.ok(orderCode + " not found");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
}