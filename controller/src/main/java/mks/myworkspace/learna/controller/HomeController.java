package mks.myworkspace.learna.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.learna.entity.Course;
import mks.myworkspace.learna.service.CourseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = {"/", "/home"})
public class HomeController {

    @Autowired
    private CourseService courseService;
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Sample init of Custom Editor

//        Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//        PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//        binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

    }

    @GetMapping
    public ModelAndView getAllCourses( ) {
		ModelAndView mav = new ModelAndView("home");

        List<Course> courses = courseService.getAllCourses();
        mav.addObject("courses", courses);
        log.debug("Danh sách khóa học: {}", courses);

        List<Course> featuredCourses = courseService.getRandomCourses();
        mav.addObject("featuredCourses", featuredCourses);
        log.debug("Khóa học nổi bật: {}", featuredCourses);

        return mav;
    }
}