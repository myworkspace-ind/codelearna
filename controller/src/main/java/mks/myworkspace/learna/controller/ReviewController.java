package mks.myworkspace.learna.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.entity.User;
import mks.myworkspace.learna.service.CourseService;
import mks.myworkspace.learna.service.ReviewService;
import mks.myworkspace.learna.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ReviewController extends BaseController{
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private UserService userService;

	// Open a course details
	@GetMapping("/course/{id}")
	public ModelAndView getCourseDetail(@PathVariable Long id, HttpServletRequest request, HttpSession httpSession) {
		initSession(request, httpSession);
		ModelAndView mav = new ModelAndView("courseDetail");
		Course course = courseService.getCourseById(id);
		List<Review> reviews = reviewService.getReviewsByCourseId(id);
		mav.addObject("course", course);
		mav.addObject("reviews", reviews);
		return mav;
	}

	// Add review
	@PostMapping("/course/{id}/review")
	public String addReview(@PathVariable Long id, @ModelAttribute Review review, Principal principal) {
		String email = "tai@gmail.com";
		User user = userService.findByEmail(email);

		if (user == null) {
			user = new User();
			user.setName("HuuTai");
			user.setUsername("Tai");
			user.setPassword("123");
			user.setEmail(email);
			user.setPhone("1234567890");
			user.setRole("student");

			user = userService.save(user);
		}
		
		String userId = getCurrentUserEid();
//		User newUser = userService.findById(userId);
		
		review.setCourse(courseService.getCourseById(id));
		review.setUser(user);
		review.setUserEid(userId);
		reviewService.addReview(review, id);
		return "redirect:/course/" + id;
	}

	// Delete review
	@PostMapping("/course/{courseId}/review/{reviewId}/delete")
	public String deleteReview(@PathVariable Long reviewId, @PathVariable Long courseId) {

		reviewService.deleteReviewById(reviewId, courseId);
		// Authentication sau

		return "redirect:/course/" + courseId;
	}

	// Edit review
	@PostMapping("/course/{courseId}/review/{reviewId}/edit")
	public String updateReview(@PathVariable Long courseId, @PathVariable Long reviewId, @ModelAttribute Review review,
			Model model) {

		reviewService.updateReviewById(reviewId, review);

		return "redirect:/course/" + courseId;
	}
}
