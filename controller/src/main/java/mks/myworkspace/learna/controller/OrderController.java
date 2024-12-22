package mks.myworkspace.learna.controller;
import mks.myworkspace.learna.entity.Order;
import mks.myworkspace.learna.entity.Order.OrderStatus;
import mks.myworkspace.learna.repository.OrderJdbcRepository;
import mks.myworkspace.learna.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderJdbcRepository orderJdbcRepository;

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
            @RequestParam BigDecimal finalPrice,
            Model model) {
    	// System.out.println("userEid:::::" + getCurrentUserEid());
        try {
            String userEid = getCurrentUserEid();
            Order order = orderService.createOrder(paymentMethod, userEid, courseId);
            // System.out.println(order);
            if ("transfer".equals(paymentMethod)) {
            	order.setAmount(finalPrice);
                String qrCodeUrl = orderService.generateQrCodeUrl(order.getOrderCode(), order.getAmount());
                model.addAttribute("orderId", order.getOrderCode());
                model.addAttribute("qrCodeUrl", qrCodeUrl);
                model.addAttribute("orderAmount", order.getAmount());
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
    
    @GetMapping("/myorders")
    public String getOrderHistory(Model model) {
        String userEid = getCurrentUserEid();
        List<Order> orders = orderService.getOrdersByUserEid(userEid);
        model.addAttribute("orders", orders);
        return "orderHistory";
    }
    
    @GetMapping("/cancel/{orderCode}")
    public String cancelOrder(@PathVariable String orderCode) {
        Optional<Order> orderOptional = orderService.getOrder(orderCode);
        if(orderOptional.isPresent()) {
        	Order order = orderOptional.get();
        	order.setStatus(OrderStatus.CANCELLED);
            orderJdbcRepository.save(order);
            return "redirect:/orders/myorders";
        } else throw new RuntimeException("Cannot find order with OrderCode: " + orderCode);
    }
    
    @GetMapping("/retry/{orderCode}")
    public String retryOrderPayment( Model model, @PathVariable String orderCode, 
    		@RequestParam("method") String paymentMethod) {
    	
    	Optional<Order> orderOptional = orderService.getOrder(orderCode);
    	if(orderOptional.isEmpty()) {
    		throw new RuntimeException("Cannot find order with OrderCode: " + orderCode); 
    	}
    	Order order = orderOptional.get();
    	if(paymentMethod.equals("transfer")) {
    		String qrCodeUrl = orderService.generateQrCodeUrl(order.getOrderCode(), order.getAmount());
            model.addAttribute("orderId", order.getOrderCode());
            model.addAttribute("qrCodeUrl", qrCodeUrl);
            model.addAttribute("orderAmount", order.getAmount());
            model.addAttribute("accountNumber", accountNumber);
            model.addAttribute("bankCode", bankCode);
            model.addAttribute("cardholder", cardholder);
            return "fragments/qr-code-payment";
    	}
    	return "redirect:/library";
    }
}
