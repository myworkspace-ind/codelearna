package mks.myworkspace.learna.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByCourseId(Long courseId);
	
	@Query("SELECT r.ratingStar FROM Review r WHERE r.course.id = :courseId")
	List<Double> findRatingsByCourseId(@Param("courseId") Long courseId);

	
	// Get average rating with courseId
	@Query("SELECT AVG(r.ratingStar) FROM Review r WHERE r.course.id = :courseId")
    Double findAverageRatingByCourseId(@Param("courseId") Long courseId);

}
