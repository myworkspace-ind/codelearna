package mks.myworkspace.learna.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.ReviewJdbcRepository;
import mks.myworkspace.learna.repository.ReviewRepository;
import mks.myworkspace.learna.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
    private ReviewJdbcRepository reviewJdbcRepository;

	@Autowired
	private CourseRepository repoCourse;

	@Override
	public List<Review> getReviewsByCourseId(Long courseId) {
		return reviewRepository.findByCourseId(courseId);
	}
//	@Override
//	public List<Review> getReviewsByCourseId(Long courseId) {
//        return reviewJdbcRepository.findByCourseId(courseId);
//    }

//	@Override
//	public void addReview(Review review, Long courseId) {
//		review.setId(null);
//		reviewRepository.save(review);
//		updateAverageRating(courseId);
//	}
	
	@Override
    public void addReview(Review review, Long courseId) {
        review.setId(null); 
        reviewJdbcRepository.save(review);
        updateAverageRating(courseId);
    }

//	@Override
//	public void deleteReviewById(Long reviewId, Long courseId) {
//		reviewRepository.deleteById(reviewId);
//		updateAverageRating(courseId);
//	}
	
	@Override
    public void deleteReviewById(Long reviewId, Long courseId) {
        reviewJdbcRepository.deleteById(reviewId); 
        updateAverageRating(courseId);
    }

//	@Override
//	public void updateReviewById(Long reviewId, Review review) {
//
//		Review existingReview = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
//
//		existingReview.setRatingStar(review.getRatingStar());
//		existingReview.setContent(review.getContent());
//
//		reviewRepository.save(existingReview);
//		updateAverageRating(existingReview.getId());
//	}
	@Override
	public void updateReviewById(Long reviewId, Review review) {
	    Review existingReview = reviewJdbcRepository.findReviewById(reviewId);
	    
	    if (existingReview == null) {
	        throw new RuntimeException("Review not found");
	    }
	    
	    existingReview.setRatingStar(review.getRatingStar());
	    existingReview.setContent(review.getContent());

	    reviewJdbcRepository.save(existingReview); 
	    updateAverageRating(existingReview.getCourse().getId());
	}


	@Override
	public double getAverageRating(Long courseId) {
		Double averageRating = reviewRepository.findAverageRatingByCourseId(courseId);
		if (averageRating != null) {
			averageRating = Math.round(averageRating * 10.0) / 10.0;
			return averageRating;
		} else {
			return 0.0;
		}
	}

	@Override
	public void updateAverageRating(Long courseId) {

		List<Double> ratings = reviewRepository.findRatingsByCourseId(courseId);

		if (ratings != null && !ratings.isEmpty()) {

			Double averageRating = getAverageRating(courseId);
			Course course = repoCourse.findById(courseId).orElse(null);
			course.setAverageRating(averageRating);

			repoCourse.save(course);
		}
	}

}
