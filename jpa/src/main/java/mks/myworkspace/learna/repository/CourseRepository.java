package mks.myworkspace.learna.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
	@Query(value = "SELECT * FROM learna_course ORDER BY RAND() LIMIT 3", nativeQuery = true)
	List<Course> findRandomCourses();

	List<Course> findBySubcategoryId(Long subcategoryId);
	
}
