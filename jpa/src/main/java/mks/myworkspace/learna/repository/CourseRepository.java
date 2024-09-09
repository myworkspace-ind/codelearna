package mks.myworkspace.learna.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
	
	// Tìm kiếm khoá học theo keyword
	List<Course> findByNameContainingIgnoreCase(String keyword);
}
