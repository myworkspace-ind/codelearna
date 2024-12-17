package mks.myworkspace.learna.controller;
import mks.myworkspace.learna.entity.Order;
import mks.myworkspace.learna.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;

    @RequestMapping
    public String show(Model model) {
        return "payment-form";
    }
    
    @Value("${payment.sepay.accountNumber}")
    private String accountNumber;
    @Value("${payment.sepay.bankCode}")
    private String bankCode;    
    @Value("${payment.sepay.cardholder}")
    private String cardholder;    
    @PostMapping
    public String processOrder(
    		HttpServletRequest request, 
    		HttpSession httpSession,
            @RequestParam String paymentMethod,
            @RequestParam Long courseId,
            Model model) {
    	// System.out.println("userEid:::::" + getCurrentUserEid());
        try {
            String userEid = getCurrentUserEid();
            Order order = orderService.createOrder(paymentMethod, userEid, courseId);
            // System.out.println(order);
            if ("transfer".equals(paymentMethod)) {
                String qrCodeUrl = orderService.generateQrCodeUrl(order.getOrderCode(), order.getAmount());
                model.addAttribute("orderId", order.getOrderCode());
                model.addAttribute("qrCodeUrl", qrCodeUrl);
                model.addAttribute("orderAmount", order.getAmount());
                model.addAttribute("accountNumber", accountNumber);
                model.addAttribute("bankCode", bankCode);
                model.addAttribute("cardholder", cardholder);
                return "fragments/qr-code-payment";
            } else if ("vnpay".equals(paymentMethod)) {
            	 request.setAttribute("order", order);
                 return "forward:/payment/vnpay";
            } else if ("ewallet".equals(paymentMethod)) {
                return "forward:/payment/pay?courseId=" + courseId;
            }
            return "redirect:/library";
        }
        catch (Exception e) {
            httpSession.setAttribute("paymentMessage", "Error occurred while processing the payment: " + e.getMessage());
            httpSession.setAttribute("alertType", "danger");
        }
    	return "redirect:/library";
    }
}
