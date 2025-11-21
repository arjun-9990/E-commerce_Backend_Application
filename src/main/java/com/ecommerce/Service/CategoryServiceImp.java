package com.ecommerce.Service;

import com.ecommerce.Exception.ApiException;
import com.ecommerce.Exception.ResourceNotFoundException;
import com.ecommerce.Model.Category;
import com.ecommerce.Payload.CategoryDTO;
import com.ecommerce.Payload.CategoryResponse;
import com.ecommerce.Repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository repo;

    private final ModelMapper modelMapper;

    public CategoryServiceImp(CategoryRepository repo, ModelMapper modelMapper) {
        this.repo = repo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Category> categoryPage = repo.findAll(pageable);

        List<Category> categories = categoryPage.getContent();

        if (categories.isEmpty()) {
            throw new ApiException("No category created till now..");
        }

        List<CategoryDTO> categoryDto = categories.stream().map(category ->
                modelMapper.map(category, CategoryDTO.class)
        ).toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDto);

        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPage(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO addTheCategories(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);

        Category saveCategory = repo.findByCategoryName(category.getCategoryName());
        if (saveCategory != null)
            throw new ApiException("Category with this name " + categoryDTO.getCategoryName() + " already exist");

        repo.save(category);

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteTheCategory(Long id) {

        Category category = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", id));

        repo.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateTheCategory(CategoryDTO categoryDTO, Long categoryId) throws ResponseStatusException {

        Category savedCategory = repo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        savedCategory = repo.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> addAllTheCategories(List<CategoryDTO> categories) {

        List<Category> categoriesData = categories.stream()
                .map(category -> modelMapper.map(category, Category.class))
                .toList();

        List<Category> savedData = repo.saveAll(categoriesData);
        List<CategoryDTO> categoriesDTo = savedData.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        return categoriesDTo;
    }
}
