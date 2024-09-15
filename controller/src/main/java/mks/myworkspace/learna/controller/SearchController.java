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
import mks.myworkspace.learna.service.SearchService;

@Controller
public class SearchController extends BaseController {

	@Autowired
	private SearchService searchService;

        // Tìm kiếm khóa học theo từ khóa
	@GetMapping("/search")
	public ModelAndView searchCourses(
	    @RequestParam(value = "keyword", required = false) String keyword,
	    @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder) {

	    List<Course> courses;

	    // Tìm kiếm khóa học theo từ khóa và sắp xếp
	    if (keyword != null && !keyword.isEmpty()) {
	        courses = searchService.searchCoursesByKeywordAndSortFilter(keyword, sortOrder);
	    } else {
	        courses = searchService.getAllCoursesSortedBySortFilter(sortOrder);
	    }

	    // Truyền dữ liệu vào view
	    ModelAndView mav = new ModelAndView("search");
	    mav.addObject("courses", courses);
	    mav.addObject("keyword", keyword);  // Đảm bảo giữ lại keyword trong view
	    mav.addObject("sortOrder", sortOrder);  // Đảm bảo giữ lại sortOrder trong view

	    return mav;
	}


}
