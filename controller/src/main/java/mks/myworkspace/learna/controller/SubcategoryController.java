package mks.myworkspace.learna.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.entity.Category;
import mks.myworkspace.learna.entity.Subcategory;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.service.CategoryService;
import mks.myworkspace.learna.service.SubcategoryService;
import mks.myworkspace.learna.service.CourseService;

@Slf4j
@Controller
public class SubcategoryController {

	@Autowired
	private SubcategoryService subcategoryService;
	
	@Autowired
	private CourseService courseService;

	@GetMapping("/subcategory/{id}")
	public ModelAndView getCoursesBySubcategory(
	        @PathVariable Long id,
	        @RequestParam(value = "keyword", required = false) String keyword,
	        @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder,
	        @RequestParam(value = "sortField", required = false, defaultValue = "createdDate") String sortField,
	        @RequestParam(value = "level", required = false) String level) {

	    ModelAndView mav = new ModelAndView("search");
	    List<Course> courses = courseService.searchCoursesByKeywordAndFilters(keyword, sortOrder, sortField, level, id);
	    mav.addObject("courses", courses);
	    mav.addObject("keyword", keyword);
	    mav.addObject("sortOrder", sortOrder);
	    mav.addObject("sortField", sortField);
	    mav.addObject("level", level);
	    mav.addObject("subcategoryId", id);
	    return mav;
	}

}
