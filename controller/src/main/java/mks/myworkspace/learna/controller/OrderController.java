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
            @RequestParam BigDecimal amount,
            @RequestParam Long userId,
            @RequestParam Long courseId,
            Model model) {
    	// System.out.println("userEid:::::" + getCurrentUserEid());

    	// userId = getCurrentUserEid();
        Order order = orderService.createOrder(paymentMethod, amount, userId, courseId);
        // System.out.println(order);
        if ("transfer".equals(paymentMethod)) {
            String qrCodeUrl = orderService.generateQrCodeUrl(order.getOrderCode(), amount);
            model.addAttribute("orderId", order.getOrderCode());
            model.addAttribute("qrCodeUrl", qrCodeUrl);
            return "fragments/qr-code-payment";
        } else if ("vnpay".equals(paymentMethod)) {
            return "redirect:/orders/";
        }
        return "redirect:/orders/" + order.getOrderCode();
    }
}
