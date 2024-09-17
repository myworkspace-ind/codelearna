package mks.myworkspace.learna.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository repo;

    @Override
    public CourseRepository getRepo() {
        return repo;
    }

    @Override
    public Course saveCourse(Course course) {
        return repo.save(course);
    }

    @Override
    public Course getCourseById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteCourse(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return repo.findAll();
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
	public List<Course> searchCoursesByKeywordAndSortFilter(String keyword, String sortOrder) {
		if ("asc".equals(sortOrder)) {
	        return repo.findByNameContainingOrDescriptionContainingOrderByCreatedDateAsc(keyword, keyword);
	    } else if("desc".equals(sortOrder)){
	        return repo.findByNameContainingOrDescriptionContainingOrderByCreatedDateDesc(keyword, keyword);
	    } else if("priceAsc".equals(sortOrder)) {
	    	return repo.findByNameContainingOrDescriptionContainingOrderByDiscountedPriceAsc(keyword, sortOrder);
	    } else {
	    	return repo.findByNameContainingOrDescriptionContainingOrderByDiscountedPriceDesc(keyword, sortOrder); 
	    }
	}


	@Override
	public List<Course> getAllCoursesSortedBySortFilter(String sortOrder) {
		if ("asc".equals(sortOrder)) {
			return repo.findAllByOrderByCreatedDateAsc();
		} else if("desc".equals(sortOrder)){
			return repo.findAllByOrderByCreatedDateDesc();
		} else if("priceAsc".equals(sortOrder)) {
			return repo.findAllByOrderByDiscountedPriceAsc();
		} else {
			return repo.findAllByOrderByDiscountedPriceDesc();
		}
	}
}
