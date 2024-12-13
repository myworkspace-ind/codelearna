package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.UserLibraryCourse;
import mks.myworkspace.learna.service.UserLibraryCourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/library")
public class UserLibraryCourseController extends BaseController{

    @Autowired
    private UserLibraryCourseService userLibraryCourseService;

    @GetMapping
    public ModelAndView getUserLibraryCoursesForDefaultUser(HttpServletRequest request, HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("userLibraryCourses");
        initSession(request, httpSession);

        String userEid = getCurrentUserEid();
        String userName = getCurrentUserDisplayName();
        mav.addObject("userEId", userEid);
        mav.addObject("userName", userName);

        // Lấy thông báo và loại alert
        String paymentMessage = (String) httpSession.getAttribute("paymentMessage");
        String alertType = (String) httpSession.getAttribute("alertType");

        if (paymentMessage != null) {
            mav.addObject("paymentMessage", paymentMessage);
            mav.addObject("alertType", alertType);
            httpSession.removeAttribute("paymentMessage");
            httpSession.removeAttribute("alertType");
        }

        // Các logic khác...
        try {
            List<UserLibraryCourse> userLibraryCourses = userLibraryCourseService.getUserLibraryCoursesByUserEid(userEid);
            mav.addObject("userLibraryCourses", userLibraryCourses);

            // Phân loại các khóa học
            List<UserLibraryCourse> purchasedCourses = userLibraryCourses.stream()
                    .filter(course -> course.getPaymentStatus() == UserLibraryCourse.PaymentStatus.PURCHASED)
                    .collect(Collectors.toList());
            mav.addObject("purchasedCourses", purchasedCourses);

            List<UserLibraryCourse> trialCourses = userLibraryCourses.stream()
                    .filter(course -> course.getPaymentStatus() == UserLibraryCourse.PaymentStatus.TRIAL)
                    .collect(Collectors.toList());
            mav.addObject("trialCourses", trialCourses);

            List<UserLibraryCourse> inProgressCourses = userLibraryCourses.stream()
                    .filter(course -> course.getProgressStatus() == UserLibraryCourse.ProgressStatus.IN_PROGRESS)
                    .collect(Collectors.toList());
            mav.addObject("inProgressCourses", inProgressCourses);

            List<UserLibraryCourse> completedCourses = userLibraryCourses.stream()
                    .filter(course -> course.getProgressStatus() == UserLibraryCourse.ProgressStatus.COMPLETE)
                    .collect(Collectors.toList());
            mav.addObject("completedCourses", completedCourses);

            log.debug("done");
        } catch (Exception e) {
            log.debug("Lỗi khi tải thư viện.");
        }

        return mav;
    }
    
    @GetMapping("/course/progress")
    @ResponseBody
    public Map<String, Integer> getCourseProgress(@RequestParam Long courseId) {
        String userEid = getCurrentUserEid(); // Method to get current user's ID
        int completionPercentage = userLibraryCourseService.calculateCompletionPercentage(userEid, courseId);
        
        Map<String, Integer> response = new HashMap<>();
        response.put("completionPercentage", completionPercentage);
        return response;
    }

    @PostMapping("/add")
    @ResponseBody
    public String addCourseToLibrary(@RequestParam Long courseId) {
        try {
        	String userEid = getCurrentUserEid();
            userLibraryCourseService.addCourseToLibrary(userEid, courseId, UserLibraryCourse.PaymentStatus.PURCHASED, UserLibraryCourse.ProgressStatus.IN_PROGRESS);
            return "Khóa học đã được thêm vào thư viện.";
        } catch (Exception e) {
            return "Lỗi khi thêm khóa học vào thư viện.";
        }
    }
}