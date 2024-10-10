package mks.myworkspace.learna.service;

import java.util.List;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.entity.Subcategory;



public interface SubcategoryService {
	List<Course> getCoursesBySubcategoryId(Long subcategoryId); 
	Subcategory getSubcategoryById(Long id);
	List<Subcategory> getAllSubcategories();
}