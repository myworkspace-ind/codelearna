package mks.myworkspace.learna.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.repository.ReviewRepository;
import mks.myworkspace.learna.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository repo;

	@Override
	public List<Review> getReviewsByCourseId(Long courseId){
		return repo.findByCourseId(courseId);
	}

	@Override
	public Review addReview(Review review) {
		review.setId(null);
		return repo.save(review);
	}
	
	@Override
	public void deleteReviewById(Long reviewId) {
		repo.deleteById(reviewId);
	}
	
	@Override
	public void updateReviewById(Long reviewId, Review review) {
		 
        Review existingReview = repo.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        
        existingReview.setRatingStar(review.getRatingStar());
        existingReview.setContent(review.getContent());

        repo.save(existingReview);
	}
	
	@Override
	public double getAverageRating(Long courseId) {
	    List<Review> reviews = repo.findByCourseId(courseId);
	    double average = reviews.stream().mapToInt(Review::getRatingStar).average().orElse(0);
	    return Math.round(average * 10.0) / 10.0;
	}


}
