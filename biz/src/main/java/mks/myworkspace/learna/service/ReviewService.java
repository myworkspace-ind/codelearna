package mks.myworkspace.learna.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.repository.ReviewRepository;

public interface ReviewService {
	
//	List<Review> getReviewsByCourseId(Long courseId);
	List<Review> getFilteredReviews(Long courseId, String sortBy);
	void addReview(Review review, Long courseId);
	void deleteReviewById(Long reviewId, Long courseId);
	void updateReviewById(Long reviewId, Review review);
	double getAverageRating(Long courseId);
	void updateAverageRating(Long courseId);
	Review getReviewById(Long reviewId);
	boolean hasUserReviewedCourse(Long courseId, String userEid);
	
	
	
}
