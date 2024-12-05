package mks.myworkspace.learna.controller;
import mks.myworkspace.learna.entity.Order;
import mks.myworkspace.learna.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
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
                return "fragments/qr-code-payment";
            } else if ("vnpay".equals(paymentMethod)) {
                return "redirect:/orders/";
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
