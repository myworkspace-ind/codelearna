package mks.myworkspace.learna.service;

import java.util.List;
import mks.myworkspace.learna.entity.Category;
import mks.myworkspace.learna.entity.Subcategory;

public interface CategoryService {
	List<Category> getAllCategories();

	List<Subcategory> findByCategoryId(Long categoryId);
}