package mks.myworkspace.learna.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Course;

@Repository
public interface SearchRepository extends JpaRepository<Course, Long> {

//	// Tìm kiếm khoá học theo keyword
//	List<Course> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String keywordName, String keywordDes);

	List<Course> findByNameContainingOrDescriptionContainingOrderByCreatedDateAsc(String name, String description);

	List<Course> findByNameContainingOrDescriptionContainingOrderByCreatedDateDesc(String name, String description);

	List<Course> findAllByOrderByCreatedDateAsc();

	List<Course> findAllByOrderByCreatedDateDesc();
	

	List<Course> findByNameContainingOrDescriptionContainingOrderByDiscountedPriceAsc(String name, String description);

	List<Course> findByNameContainingOrDescriptionContainingOrderByDiscountedPriceDesc(String name, String description);

	List<Course> findAllByOrderByDiscountedPriceAsc();

	List<Course> findAllByOrderByDiscountedPriceDesc();
}
