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
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubcategoryService subcategoryService;


    @GetMapping("/categories")
    public ModelAndView getCategories() {
        ModelAndView mav = new ModelAndView("fragments/nav"); 
        List<Category> categories = categoryService.getAllCategories();
        
        log.debug("Categories size: " + categories.size());
        mav.addObject("categories", categories);
        return mav;
    }

 
    @GetMapping("/category/{id}")
    public ModelAndView getSubcategoriesByCategory(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("fragments/nav");
        List<Subcategory> subcategories = categoryService.findByCategoryId(id);
        log.debug("Subcategories size: " + subcategories.size());
        List<Category> categories = categoryService.getAllCategories();
        mav.addObject("subcategories", subcategories);
        mav.addObject("categories", categories);
        return mav;
    }

    @GetMapping("/subcategory/{id}")
    public ModelAndView getCoursesBySubcategory(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("search"); 
        List<Course> courses = subcategoryService.getCoursesBySubcategoryId(id);
        Subcategory subcategory = subcategoryService.getSubcategoryById(id); 
        List<Category> categories = categoryService.getAllCategories();
        mav.addObject("courses", courses);
        mav.addObject("subcategory", subcategory);
        mav.addObject("categories", categories); 
        return mav;
    }
}
