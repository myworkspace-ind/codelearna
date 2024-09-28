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
		return repo.save(review);
	}
	
	@Override
	public void deleteById(Long reviewId) {
		repo.deleteById(reviewId);
	}
	
	@Override
	public double getAverageRating(Long courseId) {
		List<Review> reviews = repo.findByCourseId(courseId);
		return reviews.stream().mapToInt(Review::getRatingStar).average().orElse(0);
	}

}
