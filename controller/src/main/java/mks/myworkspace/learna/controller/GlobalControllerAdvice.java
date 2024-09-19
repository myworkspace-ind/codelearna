package mks.myworkspace.learna.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import mks.myworkspace.learna.entity.Category;
import mks.myworkspace.learna.entity.Subcategory;
import mks.myworkspace.learna.service.CategoryService;
import mks.myworkspace.learna.service.ParameterService;

@ControllerAdvice
public class GlobalControllerAdvice {

//    @Autowired
//    private CategoryService categoryService;
    
    @Autowired
    private ParameterService parameterService;


//    @ModelAttribute("categories")
//    public List<Category> getCategories() {
//        return categoryService.getAllCategories();
//    }
    
//    @ModelAttribute("category/{id}")
//    public List<Subcategory> getSubcategoriesById(Long id){
//    	return categoryService.getSubcategoriesByCategoryId(id);
//    }
    
   
    @ModelAttribute("logoUrl")
    public String addLogoUrlToModel() {
        return parameterService.getLogoUrl();
    }
}
