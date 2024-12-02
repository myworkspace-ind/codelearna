package mks.myworkspace.learna.service;

import java.util.List;

import org.springframework.data.domain.Page;

import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.repository.ReviewRepository;

public interface ReviewService {
	
//	List<Review> getReviewsByCourseId(Long courseId);
	Page<Review> getReviewsByCourseId(Long courseId, int page, int size);
	void addReview(Review review, Long courseId);
	void deleteReviewById(Long reviewId, Long courseId);
	void updateReviewById(Long reviewId, Review review);
	double getAverageRating(Long courseId);
	void updateAverageRating(Long courseId);
	
	boolean hasUserReviewedCourse(Long courseId, String userEid);
	
	
	
}
