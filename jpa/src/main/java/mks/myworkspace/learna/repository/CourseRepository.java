package mks.myworkspace.learna.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Parameter;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

	// Truy vấn ngẫu nhiên 3 khóa học
	@Query(value = "SELECT * FROM learna_course WHERE status = 'ACTIVE' ORDER BY RAND() LIMIT 3", nativeQuery = true)
	List<Course> findRandomCourses();

	// Tìm khóa học theo danh mục con
	List<Course> findBySubcategoryId(Long subcategoryId);

	 @Query("SELECT COUNT(c.id) FROM Course c")
	    int getTotalCourses();

	 @Query("SELECT c FROM Course c WHERE c.status = :status")
	 List<Course> findCourseByStatus(@Param("status") String status);
	 
	 @Query("SELECT c FROM Course c WHERE c.status = 'ACTIVE'")
	 List<Course> findAllActiveCourse();
//	// Tìm kiếm khóa học với các bộ lọc (keyword, difficulty level, average rating)
//	@Query("SELECT c FROM Course c "
//			+ "WHERE (:keyword IS NULL OR c.name LIKE %:keyword% OR c.description LIKE %:keyword%) "
//			+ "AND (:level IS NULL OR c.difficultyLevel = :level) "
//			+ "AND (:averageRating IS NULL OR c.averageRating >= :averageRating) " + "AND c.status = 'active'")
//	List<Course> findCoursesByFilters(@Param("keyword") String keyword, @Param("level") Parameter level,
//			@Param("averageRating") Double averageRating);
//
//	// Tìm kiếm khóa học trong danh mục con với các bộ lọc
//	@Query("SELECT c FROM Course c JOIN c.subcategory s " + "WHERE s.id = :subcategoryId "
//			+ "AND (:keyword IS NULL OR c.name LIKE %:keyword% OR c.description LIKE %:keyword%) "
//			+ "AND (:level IS NULL OR c.difficultyLevel = :level) "
//			+ "AND (:averageRating IS NULL OR c.averageRating >= :averageRating) " + "AND c.status = 'active'")
//	List<Course> findCoursesBySubcategoryAndFilters(@Param("subcategoryId") Long subcategoryId,
//			@Param("keyword") String keyword, @Param("level") Parameter level,
//			@Param("averageRating") Double averageRating);
	
	
	// Tìm kiếm khóa học với các bộ lọc (keyword, difficulty level, average rating)
		@Query("SELECT c FROM Course c "
		+ "WHERE (:keyword IS NULL OR c.name LIKE %:keyword% OR c.description LIKE %:keyword%) "
		+ "AND (:level IS NULL OR c.difficultyLevel = :level) "
		+ "AND (:averageRating IS NULL OR ((c.averageRating >= :averageRating) AND (c.averageRating <= :averageRating + 1 )) )" 
		+ "AND c.status = 'active'")
		Page<Course> findCoursesByFilters(@Param("keyword") String keyword, @Param("level") Parameter level,
				@Param("averageRating") Double averageRating, Pageable pageable);

		// Tìm kiếm khóa học trong danh mục con với các bộ lọc
		@Query("SELECT c FROM Course c JOIN c.subcategory s " + "WHERE s.id = :subcategoryId "
				+ "AND (:keyword IS NULL OR c.name LIKE %:keyword% OR c.description LIKE %:keyword%) "
				+ "AND (:level IS NULL OR c.difficultyLevel = :level) "
				+ "AND (:averageRating IS NULL OR c.averageRating >= :averageRating) " + "AND c.status = 'active'")
		Page<Course> findCoursesBySubcategoryAndFilters(@Param("subcategoryId") Long subcategoryId,
				@Param("keyword") String keyword, @Param("level") Parameter level,
				@Param("averageRating") Double averageRating, Pageable pageable);

}
