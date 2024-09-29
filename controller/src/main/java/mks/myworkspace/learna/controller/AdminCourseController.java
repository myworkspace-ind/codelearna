package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    // Hiển thị trang admin home
    @GetMapping
    public String showAdminHomePage() {
        return "adminHome"; // Trả về trang adminHome.html
    }

    // Hiển thị trang thêm khóa học
    @GetMapping("/save-course")
    public ModelAndView showSaveCoursePage() {
        ModelAndView mav = new ModelAndView("Admin/addCourse");
        mav.addObject("course", new Course()); 
        return mav;
    }

    // Xử lý lưu khóa học
    @PostMapping("/save-course")
    public String saveCourse(@ModelAttribute("course") Course course) {
        courseService.saveCourse(course);
        return "redirect:/admin/save-course"; // Sau khi lưu, quay lại trang form
    }
}
