package mks.myworkspace.learna.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.service.CourseService;
import mks.myworkspace.learna.service.PaymentService;
import mks.myworkspace.learna.service.ReviewService;
import mks.myworkspace.learna.service.UserLibraryCourseService;

@Slf4j
@Controller
public class CourseController extends BaseController {
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private UserLibraryCourseService userLibraryCourseService;

	@GetMapping("/course/{id}")
	public ModelAndView getCourseDetail(@PathVariable Long id, @RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String sortBy, HttpServletRequest request, HttpSession httpSession) {

		initSession(request, httpSession);
		String userEid = getCurrentUserEid();
		ModelAndView mav = new ModelAndView("courseDetail");

		Course course = courseService.getCourseById(id);
		Double balance = paymentService.getBalance(userEid);

		List<Review> filteredReviews = reviewService.getFilteredReviews(id, sortBy);

		int pageSize = 5;
		int totalReviews = filteredReviews.size();
		int totalPages = (int) Math.ceil((double) totalReviews / pageSize);

		int start = Math.min(page * pageSize, totalReviews);
		int end = Math.min(start + pageSize, totalReviews);
		List<Review> paginatedReviews = filteredReviews.subList(start, end);

		boolean hasPurchased = userLibraryCourseService.isCoursePurchased(userEid, id);
		boolean hasReviewed = reviewService.hasUserReviewedCourse(id, userEid);
		
		Object discountedPrice = httpSession.getAttribute("discountedPrice");
	    Object voucherCode = httpSession.getAttribute("voucherCode");

		mav.addObject("course", course);
		mav.addObject("reviews", paginatedReviews);
		mav.addObject("userEid", userEid);
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", totalPages);
		mav.addObject("balance", balance);
		mav.addObject("hasPurchased", hasPurchased);
		mav.addObject("hasReviewed", hasReviewed);
		
		if (discountedPrice != null) {
	        mav.addObject("discountedPrice", discountedPrice);
	        mav.addObject("voucherApplied", true);
	    }
		return mav;
	}
	
	@PostMapping("/voucher/apply")
    @ResponseBody
    public Map<String, Object> applyVoucher(@RequestParam("courseId") Long courseId,
                                            @RequestParam("voucher") String voucherCode, HttpSession httpSession) {
        Map<String, Object> response = new HashMap<>();
        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            response.put("success", false);
            response.put("error", "Course not found!");
            return response;
        }

        double originalPrice = course.getOriginalPrice();
        double discountedPrice = originalPrice;

        // Xử lý mã giảm giá
        switch (voucherCode) {
            case "voucher1":
                discountedPrice = originalPrice * 0.9; // Giảm 10%
                break;
            case "voucher2":
                discountedPrice = originalPrice - 50000; // Giảm 50,000 VNĐ
                break;
            default:
                response.put("success", false);
                response.put("error", "Invalid voucher code!");
                return response;
        }

        discountedPrice = Math.max(discountedPrice, 0);

        // Lưu thông tin vào session
        httpSession.setAttribute("discountedPrice", discountedPrice);
        httpSession.setAttribute("voucherCode", voucherCode);

        // Trả về JSON
        response.put("success", true);
        response.put("originalPrice", String.format("%.0f", originalPrice));
        response.put("discountedPrice", String.format("%.0f", discountedPrice));
        return response;
    }
}
