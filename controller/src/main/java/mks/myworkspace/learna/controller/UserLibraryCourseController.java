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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/library")
public class UserLibraryCourseController {

    @Autowired
    private UserLibraryCourseService userLibraryCourseService;

    @GetMapping
    public ModelAndView getUserLibraryCoursesForDefaultUser() {
        ModelAndView mav = new ModelAndView("userLibraryCourses");

        Long userId = (long) 1; // Mặc định userId là 1

        List<UserLibraryCourse> userLibraryCourses = userLibraryCourseService.getUserLibraryCoursesByUserId(userId);
        mav.addObject("userLibraryCourses", userLibraryCourses);

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

        log.debug("Danh sách khóa học đã lưu: {}", userLibraryCourses);
        log.debug("done");

        return mav;
    }

    @PostMapping("/add")
    @ResponseBody
    public String addCourseToLibrary(@RequestParam Long userId, @RequestParam Long courseId) {
        try {
            userLibraryCourseService.addCourseToLibrary(userId, courseId, UserLibraryCourse.PaymentStatus.PURCHASED, UserLibraryCourse.ProgressStatus.IN_PROGRESS);
            return "Khóa học đã được thêm vào thư viện.";
        } catch (Exception e) {
            log.error("Lỗi khi thêm khóa học vào thư viện", e);
            return "Lỗi khi thêm khóa học vào thư viện.";
        }
    }
}