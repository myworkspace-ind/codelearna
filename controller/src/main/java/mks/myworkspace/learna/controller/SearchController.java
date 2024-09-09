package mks.myworkspace.learna.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchCourses(@RequestParam(value = "keyword", required = false) String keyword, 
                                      HttpServletRequest request, HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("search");

//        initSession(request, httpSession);
        
        mav.addObject("currentSiteId", getCurrentSiteId());
        mav.addObject("userDisplayName", getCurrentUserDisplayName());

        // Tìm kiếm khóa học theo từ khóa
        List<Course> courses;
        if (keyword != null && !keyword.isEmpty()) {
            courses = searchService.searchCoursesByKeyword(keyword);
        } else {
            courses = searchService.getAllCourses();
        }

        
        mav.addObject("courses", courses);
        mav.addObject("keyword", keyword); 

        return mav;
    }
}
