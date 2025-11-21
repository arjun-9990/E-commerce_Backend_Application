package com.ecommerce.Service;

import com.ecommerce.Payload.CategoryDTO;
import com.ecommerce.Payload.CategoryResponse;

import java.util.List;


public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO addTheCategories(CategoryDTO categoryDTO);

    CategoryDTO deleteTheCategory(Long id);

    CategoryDTO updateTheCategory(CategoryDTO categoryDTO, Long categoryId);

    // Add categories in bulk
    List<CategoryDTO> addAllTheCategories(List<CategoryDTO> categories);

}
