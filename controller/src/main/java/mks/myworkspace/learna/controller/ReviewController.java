package mks.myworkspace.learna.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.service.CourseService;
import mks.myworkspace.learna.service.PaymentService;
import mks.myworkspace.learna.service.ReviewService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ReviewController extends BaseController {
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private PaymentService paymentService;

	// Add review
	@PostMapping("/course/{id}/review")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addReview(@PathVariable Long id, @ModelAttribute Review review,
	        Principal principal, HttpServletRequest request, HttpSession httpSession) {
	    initSession(request, httpSession);
	    String userId = getCurrentUserEid();
	    Map<String, Object> response = new HashMap<>();

	    try {
	        review.setCourse(courseService.getCourseById(id));
	        review.setUserEid(userId);

	        reviewService.addReview(review, id);

	        response.put("success", true);
	        response.put("message", "Review added successfully!");

	        return ResponseEntity.ok()
	                             .header(HttpHeaders.CONTENT_TYPE, "application/json")
	                             .body(response);

	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "Failed to add review. Please try again.");

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .header(HttpHeaders.CONTENT_TYPE, "application/json")
	                             .body(response);
	    }
	}


	// Delete review
	@PostMapping("/course/{courseId}/review/{reviewId}/delete")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> deleteReview(@PathVariable Long courseId, @PathVariable Long reviewId) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        reviewService.deleteReviewById(reviewId, courseId);

	        response.put("success", true);
	        response.put("message", "Review has been deleted successfully!");
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_TYPE, "application/json")
	                .body(response);
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "Failed to delete review. Please try again.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .header(HttpHeaders.CONTENT_TYPE, "application/json")
	                .body(response);
	    }
	}


	// Edit review
	@PostMapping("/course/{courseId}/review/{reviewId}/edit")
	public String updateReview(@PathVariable Long courseId, @PathVariable Long reviewId, @ModelAttribute Review review,
			Model model) {

		reviewService.updateReviewById(reviewId, review);

		return "redirect:/course/" + courseId;
	}
}
