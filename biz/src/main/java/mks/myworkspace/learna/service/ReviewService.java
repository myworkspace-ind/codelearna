package mks.myworkspace.learna.service;

import java.util.List;

import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.repository.ReviewRepository;

public interface ReviewService {
	
	List<Review> getReviewsByCourseId(Long courseId);
	Review addReview(Review review);
	double getAverageRating(Long courseId);
	
}
