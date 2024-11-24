package mks.myworkspace.learna.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.service.CourseService;
import mks.myworkspace.learna.service.PaymentService;
import mks.myworkspace.learna.service.ReviewService;


@Slf4j
@Controller
public class CourseController extends BaseController{
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private PaymentService paymentService;

	// Open a course details
	@GetMapping("/course/{id}")
	public ModelAndView getCourseDetail(@PathVariable Long id, @RequestParam(defaultValue = "0") int page,
			HttpServletRequest request, HttpSession httpSession) {

		initSession(request, httpSession);
		String userId = getCurrentUserEid();
		ModelAndView mav = new ModelAndView("courseDetail");

		Course course = courseService.getCourseById(id);
		Double balance = paymentService.getBalance(userId);

		int pageSize = 5;
		Page<Review> reviewPage = reviewService.getReviewsByCourseId(id, page, pageSize);

		List<Review> reviews = reviewPage.getContent();
		int totalPages = reviewPage.getTotalPages();

		mav.addObject("course", course);
		mav.addObject("reviews", reviews);
		mav.addObject("userId", userId);
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", totalPages);
		mav.addObject("balance", balance);

		return mav;
	}

}
