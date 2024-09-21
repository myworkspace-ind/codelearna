package mks.myworkspace.learna.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Subcategory;
import mks.myworkspace.learna.repository.CourseRepository;
import mks.myworkspace.learna.repository.SubcategoryRepository;
import mks.myworkspace.learna.service.SubcategoryService;

import java.util.List;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {

//    @Autowired
//    private SubcategoryRepository subcategoryRepository;

	@Autowired
	private CourseRepository courseRepository; 
	@Autowired
	private SubcategoryRepository subcategoryRepository;

	@Override
	public List<Course> getCoursesBySubcategoryId(Long subcategoryId) {
		return courseRepository.findBySubcategoryId(subcategoryId);
	}

	@Override
	public Subcategory getSubcategoryById(Long id) {
		return subcategoryRepository.findById(id).orElse(null);
	}
	
	@Override
	public List<Subcategory> getAllSubcategories() {
		return subcategoryRepository.findAll();
	}
}