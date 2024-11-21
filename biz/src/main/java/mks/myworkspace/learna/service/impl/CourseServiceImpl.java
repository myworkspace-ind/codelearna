package mks.myworkspace.learna.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Parameter;
import mks.myworkspace.learna.repository.CourseJdbcRepository;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.ParameterRepository;
import mks.myworkspace.learna.service.CourseService;
import mks.myworkspace.learna.service.ReviewService;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseRepository repo;
	
	@Autowired
	private CourseJdbcRepository courseJdbcRepository;
	
	@Autowired
	private ParameterRepository parameterRepository;

	@Autowired
	private ReviewService reviewService;

	@Override
	public CourseRepository getRepo() {
		return repo;
	}

//	@Override
//	public Course saveCourse(Course course) {
//		return repo.save(course);
//	}
	
	@Override
	public Course saveCourse(Course course) {
		return courseJdbcRepository.save(course);
	}

	@Override
	public Course getCourseById(Long id) {
		Course course = repo.findById(id).orElse(null);
		Double averageRating = reviewService.getAverageRating(course.getId());
		course.setAverageRating(averageRating);
		return course;
	}

	@Override
	public void deleteCourse(Long id) {
		courseJdbcRepository.deleteById(id);
	}

	@Override
	public List<Course> getAllCourses() {
		List<Course> courses = repo.findAll();
		for(Course course : courses) {
			reviewService.updateAverageRating(course.getId());
		}
		return courses;
	}

	@Override
	public List<Course> getRandomCourses() {
		return repo.findRandomCourses();
	}

	@Override
	public List<Course> getCoursesBySubcategory(Long subcategoryId) {
		return repo.findBySubcategoryId(subcategoryId);
	}

	@Override
	public List<Course> searchCoursesByKeywordAndFilters(String keyword, String sortOrder, String sortField, String level, String averageRating) {
	    Sort sort = Sort.by(sortField);
	    sort = "desc".equalsIgnoreCase(sortOrder) ? sort.descending() : sort.ascending();
	    Pageable pageable = PageRequest.of(0, 20, sort);

	    Parameter difficultyLevelParameter = null;
	    if (level != null) {
	        difficultyLevelParameter = parameterRepository.findByParamValue(level.toUpperCase()); 
	    }

	    Double ratingValue = null;
	    if (averageRating != null) {
	        try {
	            ratingValue = Double.valueOf(averageRating);
	        } catch (NumberFormatException e) {
	            log.debug("Erorr occurs while formating ratingValue into Double type" + e.getMessage());
	        }
	    }

	    return repo.findCoursesByFilters(keyword, difficultyLevelParameter, ratingValue, pageable);
	}

	@Override
	public List<Course> searchCoursesByKeywordAndFilters(String keyword, String sortOrder, String sortField, String level, Long subcategoryId, String rating) {
	    Sort sort = Sort.by(sortField);
	    sort = "desc".equalsIgnoreCase(sortOrder) ? sort.descending() : sort.ascending();
	    Pageable pageable = PageRequest.of(0, 20, sort);

	    Parameter difficultyLevelParameter = null;
	    if (level != null) {
	        difficultyLevelParameter = parameterRepository.findByParamValue(level.toUpperCase()); 
	    }

	    Double ratingValue = null;
	    if (rating != null) {
	        try {
	            ratingValue = Double.valueOf(rating);
	        } catch (NumberFormatException e) {
	        	log.debug("Erorr occurs while formating ratingValue into Double type" + e.getMessage());
	        }
	    }

	    if (subcategoryId != null) {
	        return repo.findCoursesBySubcategoryAndFilters(subcategoryId, keyword, difficultyLevelParameter, ratingValue, pageable);
	    } else {
	        return repo.findCoursesByFilters(keyword, difficultyLevelParameter, ratingValue, pageable);
	    }
	}


}
