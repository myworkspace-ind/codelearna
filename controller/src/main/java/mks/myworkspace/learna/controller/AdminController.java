package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.service.CategoryService;
import mks.myworkspace.learna.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private CategoryService categoryService;

    // Hiển thị trang admin home
    @GetMapping
    public String showAdminHomePage() {
        return "adminHome"; // Trả về trang adminHome.html
    }
    @GetMapping("/listCourse")
    public ModelAndView loadCoursesFragment() {
        ModelAndView mav = new ModelAndView("fragments/adminListCourse :: coursesContent");
        mav.addObject("courses", courseService.getAllCourses());
        return mav;
    }
    

    @GetMapping("/add-course")
    public ModelAndView showAddCoursePage() {
        ModelAndView mav = new ModelAndView("fragments/adminAddCourse :: addCourse");
        mav.addObject("categories", categoryService.getAllCategories()); // Lấy danh sách Category
        return mav;
    }
  
}

