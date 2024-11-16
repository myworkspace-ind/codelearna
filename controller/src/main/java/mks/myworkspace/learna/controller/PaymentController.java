package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.service.PaymentService;
import mks.myworkspace.learna.service.UserLibraryCourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import lombok.extern.slf4j.Slf4j;

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
        ModelAndView mav = new ModelAndView("redirect:/library"); // Chuyển hướng đến thư viện
        try {
            String userEid = getCurrentUserEid();
            boolean success = paymentService.payForCourse(userEid, courseId);
            if (success) {
                // Cập nhật số dư mới lên session
                Double newBalance = paymentService.getBalance(userEid);
                httpSession.setAttribute("userBalance", newBalance);
                httpSession.setAttribute("paymentMessage", "Payment successful and the course has been added to your library.");
            } else {
                httpSession.setAttribute("paymentMessage", "Payment failed. Please check your balance.");
            }
        } catch (Exception e) {
            httpSession.setAttribute("paymentMessage", "Error occurred while processing the payment.");
        }
        return mav;
    }
}