package mks.myworkspace.learna.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.learna.entity.Category;
import mks.myworkspace.learna.service.CategoryService;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories") 
    public ModelAndView getCategories() {
        ModelAndView mav = new ModelAndView("categoryList");
        List<Category> categories = categoryService.getAllCategories(); 
        mav.addObject("categories", categories);
        return mav;
    }
}