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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.entity.Voucher;
import mks.myworkspace.learna.service.CourseService;
import mks.myworkspace.learna.service.PaymentService;
import mks.myworkspace.learna.service.ReviewService;
import mks.myworkspace.learna.service.UserLibraryCourseService;
import mks.myworkspace.learna.service.VoucherService;

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
	private VoucherService voucherService;

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
		
		List<Voucher> vouchers = voucherService.getAllVouchers();
		
		List<Voucher> course_vouchers = voucherService.getAllVoucherByCourseId(id);

		List<Review> filteredReviews = reviewService.getFilteredReviews(id, sortBy);

		int pageSize = 5;
		int totalReviews = filteredReviews.size();
		int totalPages = (int) Math.ceil((double) totalReviews / pageSize);

		int start = Math.min(page * pageSize, totalReviews);
		int end = Math.min(start + pageSize, totalReviews);
		List<Review> paginatedReviews = filteredReviews.subList(start, end);

		boolean hasPurchased = userLibraryCourseService.isCoursePurchased(userEid, id);
		boolean hasReviewed = reviewService.hasUserReviewedCourse(id, userEid);

		mav.addObject("course", course);
		mav.addObject("vouchers", vouchers);
		mav.addObject("course_vouchers", course_vouchers);
		mav.addObject("reviews", paginatedReviews);
		mav.addObject("userEid", userEid);
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", totalPages);
		mav.addObject("balance", balance);
		mav.addObject("hasPurchased", hasPurchased);
		mav.addObject("hasReviewed", hasReviewed);

		return mav;
	}

}
