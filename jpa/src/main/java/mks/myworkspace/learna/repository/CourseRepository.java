package mks.myworkspace.learna.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
	@Query(value = "SELECT * FROM learna_course ORDER BY RAND() LIMIT 3", nativeQuery = true)
	List<Course> findRandomCourses();

	List<Course> findBySubcategoryId(Long subcategoryId);

	// Normal search
	@Query("SELECT c FROM Course c "
			+ "WHERE (:keyword IS NULL OR c.name LIKE %:keyword% OR c.description LIKE %:keyword%) "
			+ "AND (:level IS NULL OR c.difficultyLevel = :level) " + "AND (:averageRating IS NULL OR c.averageRating >= :averageRating)")
	List<Course> findCoursesByFilters(@Param("keyword") String keyword, @Param("level") Course.DifficultyLevel level,
			@Param("averageRating") Double averageRating, Pageable pageable);

	// Search in sub category
	@Query("SELECT c FROM Course c JOIN c.subcategory s " + "WHERE s.id = :subcategoryId "
			+ "AND (:keyword IS NULL OR c.name LIKE %:keyword% OR c.description LIKE %:keyword%) "
			+ "AND (:level IS NULL OR c.difficultyLevel = :level) " + "AND (:averageRating IS NULL OR c.averageRating >= :averageRating)")
	List<Course> findCoursesBySubcategoryAndFilters(@Param("subcategoryId") Long subcategoryId,
			@Param("keyword") String keyword, @Param("level") Course.DifficultyLevel level,
			@Param("averageRating") Double averageRating,
			Pageable pageable);

}
