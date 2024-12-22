package mks.myworkspace.learna.service.impl;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.entity.Category;
import mks.myworkspace.learna.entity.Parameter;
import mks.myworkspace.learna.entity.Subcategory;
import mks.myworkspace.learna.repository.CategoryJdbcRepository;
import mks.myworkspace.learna.repository.CategoryRepository;
import mks.myworkspace.learna.repository.ParameterJdbcRepository;
import mks.myworkspace.learna.repository.ParameterRepository;
import mks.myworkspace.learna.repository.SubcategoryJdbcRepository;
import mks.myworkspace.learna.repository.SubcategoryRepository;
import mks.myworkspace.learna.service.DBInit;

@Slf4j
@Service
public class DBInitImpl implements DBInit {
	@Autowired
	private ParameterRepository parameterRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SubcategoryRepository subcategoryRepository;
	@Autowired
	private ParameterJdbcRepository parameterJdbcRepository;
	@Autowired
	private CategoryJdbcRepository categoryJdbcRepository;
	@Autowired
	private SubcategoryJdbcRepository subcategoryJdbcRepository;
	@Value("classpath:initDB/data.json")
    private Resource resource;
	
	@PostConstruct
    public void init() {
        if (parameterRepository.count() == 0 && categoryRepository.count() == 0 && subcategoryRepository.count() == 0) {
            try {
            	InputStream inputStream = resource.getInputStream();
                String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                // Phân tích JSON
                JSONObject root = new JSONObject(jsonContent);

                // Lưu Parameters
                JSONArray parametersArray = root.getJSONArray("parameters");
                Parameter parameter;
                for (int i = 0; i < parametersArray.length(); i++) {
                    JSONObject paramJson = parametersArray.getJSONObject(i);

                    parameter = new Parameter();
                    parameter.setId(paramJson.getLong("id"));
                    parameter.setDescription(paramJson.optString("description", null));
                    parameter.setParamKey(paramJson.getString("param_key"));
                    parameter.setParamValue(paramJson.getString("param_value"));
                    parameter.setSeqno(paramJson.optInt("seqno", 0));
                    parameter.setStatus(paramJson.getString("status"));

                    parameterJdbcRepository.save(parameter);
                }

                // Lưu Categories
                JSONArray categoriesArray = root.getJSONArray("categories");
                for (int i = 0; i < categoriesArray.length(); i++) {
                    JSONObject categoryJson = categoriesArray.getJSONObject(i);

                    Category category = new Category();

                    parameter = parameterRepository.findById(categoryJson.getLong("parameter_id")).orElse(null);
                    category.setParameter(parameter);  // Thiết lập Parameter cho Category

                    categoryJdbcRepository.save(category);
                }

                // Lưu Subcategories
                JSONArray subcategoriesArray = root.getJSONArray("subcategories");
                for (int i = 0; i < subcategoriesArray.length(); i++) {
                    JSONObject subcategoryJson = subcategoriesArray.getJSONObject(i);

                    Subcategory subcategory = new Subcategory();

                    // Lấy các đối tượng liên quan
                    Category category = categoryRepository.findById(subcategoryJson.optLong("category_id", 0)).orElse(null);
                    parameter = parameterRepository.findById(subcategoryJson.getLong("parameter_id")).orElse(null);
                    subcategory.setCategory(category);
                    subcategory.setParameter(parameter);

                    subcategoryJdbcRepository.save(subcategory);
                }

                log.info("Data loaded successfully!");
            } catch (Exception e) {
                log.error("Error when loading data: " + e);
            }
        }
    }
}
