package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.service.CategoryService;
import mks.myworkspace.learna.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    // Admin homepage
    @GetMapping
    public String showAdminHomePage() {
        return "adminHome"; 
    }

    // Display list of courses in the admin panel
    @GetMapping("/listCourse")
    public ModelAndView showCoursesList() {
        ModelAndView mav = new ModelAndView("fragments/adminListCourse :: coursesContent");
        mav.addObject("courses", courseService.getAllCourses());
        return mav;
    }


    // Show the "Add Course" form
    @GetMapping("/add-course")
    public ModelAndView showAddCourseForm() {
        ModelAndView mav = new ModelAndView("fragments/adminAddCourse :: addCourse");
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    @GetMapping("/courses/edit/{id}")
    public ModelAndView showEditCourseForm(@PathVariable("id") Long id) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return new ModelAndView("redirect:/admin/list-course");
        }

        ModelAndView mav = new ModelAndView("fragments/adminEditCourse :: editCourse");
        mav.addObject("course", course);
        return mav;
    }




    // Handle the submission of the edit course form
    @PostMapping("/courses/edit/{id}")
    public ModelAndView editCourse(@PathVariable("id") Long id, @ModelAttribute("course") Course course) {
        Course existingCourse = courseService.getCourseById(id);
        if (existingCourse == null) {
            return new ModelAndView("redirect:/admin/list-course");
        }

        // Update the course details
        existingCourse.setName(course.getName());
        existingCourse.setOriginalPrice(course.getOriginalPrice());
        existingCourse.setDiscountedPrice(course.getDiscountedPrice());
        existingCourse.setImageUrl(course.getImageUrl());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setDifficultyLevel(course.getDifficultyLevel());

        // Save updated course
        courseService.saveCourse(existingCourse);

        return new ModelAndView("redirect:/admin/list-course");
    }

    // Handle deleting a course
    @GetMapping("/delete-course/{id}")
    public ModelAndView deleteCourse(@PathVariable("id") Long id) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            courseService.deleteCourse(id);
        }

        return new ModelAndView("redirect:/admin/list-course");
    }
}
