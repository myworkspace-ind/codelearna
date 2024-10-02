package mks.myworkspace.learna.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Review;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.ReviewRepository;
import mks.myworkspace.learna.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository repo;

	@Autowired
	private CourseRepository repoCourse;

	@Override
	public List<Review> getReviewsByCourseId(Long courseId) {
		return repo.findByCourseId(courseId);
	}

	@Override
	public void addReview(Review review, Long courseId) {
		review.setId(null);
		repo.save(review);
		updateAverageRating(courseId);
	}

	@Override
	public void deleteReviewById(Long reviewId, Long courseId) {
		repo.deleteById(reviewId);
		updateAverageRating(courseId);
	}

	@Override
	public void updateReviewById(Long reviewId, Review review) {

		Review existingReview = repo.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));

		existingReview.setRatingStar(review.getRatingStar());
		existingReview.setContent(review.getContent());

		repo.save(existingReview);
		updateAverageRating(existingReview.getId());
	}

	@Override
	public double getAverageRating(Long courseId) {
		Double averageRating = repo.findAverageRatingByCourseId(courseId);
		if (averageRating != null) {
			averageRating = Math.round(averageRating * 10.0) / 10.0;
			return averageRating;
		} else {
			return 0.0;
		}
	}

	@Override
	public void updateAverageRating(Long courseId) {

		List<Double> ratings = repo.findRatingsByCourseId(courseId);

		if (ratings != null && !ratings.isEmpty()) {

			Double averageRating = getAverageRating(courseId);
			Course course = repoCourse.findById(courseId).orElse(null);
			course.setAverageRating(averageRating);

			repoCourse.save(course);
		}
	}

}
