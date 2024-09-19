package mks.myworkspace.learna.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.service.CourseService;

@Controller
public class SearchController extends BaseController {

	@Autowired
	private CourseService courseService;
	
	 @GetMapping("/search")
	    public ModelAndView searchCourses(
	            @RequestParam(value = "keyword", required = false) String keyword,
	            @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder,
	            @RequestParam(value = "sortField", required = false, defaultValue = "createdDate") String sortField,
	            @RequestParam(value = "level", required = false) String level) {

	        ModelAndView mav = new ModelAndView("search");

	        
	        List<Course> courses = courseService.searchCoursesByKeywordAndFilters(keyword, sortOrder, sortField, level);

	    
	        mav.addObject("courses", courses);
	        mav.addObject("keyword", keyword);
	        mav.addObject("sortOrder", sortOrder);
	        mav.addObject("sortField", sortField);
	        mav.addObject("level", level);

	        return mav;
	    }

}
