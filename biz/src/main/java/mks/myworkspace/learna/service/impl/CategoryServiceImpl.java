package mks.myworkspace.learna.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mks.myworkspace.learna.entity.Category;
import mks.myworkspace.learna.entity.Subcategory;
import mks.myworkspace.learna.repository.CategoryRepository;
import mks.myworkspace.learna.repository.SubcategoryRepository;
import mks.myworkspace.learna.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Subcategory> findByCategoryId(Long categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId);
    }
}