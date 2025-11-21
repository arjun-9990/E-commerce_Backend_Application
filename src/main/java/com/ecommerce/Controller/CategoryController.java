package com.ecommerce.Controller;

import com.ecommerce.Config.AppConstands;
import com.ecommerce.Payload.CategoryDTO;
import com.ecommerce.Payload.CategoryResponse;
import com.ecommerce.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {

        this.service = service;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategory(
            @RequestParam(name = "PageNumber", defaultValue = AppConstands.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "PageSize", defaultValue = AppConstands.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstands.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstands.SORT_ORDER, required = false) String sortOrder
    ) {
        return new ResponseEntity<CategoryResponse>(service.getAllCategories(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(service.addTheCategories(categoryDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable long categoryId) {
        CategoryDTO deletedCategory = service.deleteTheCategory(categoryId);
        return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable("categoryId") Long categoryId) {
        CategoryDTO savedCategoryDTO = service.updateTheCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }


    @PostMapping("/admin/addAllCategories")
    public ResponseEntity<List<CategoryDTO>> createAllCategory(@RequestBody List<CategoryDTO> categories) {
        System.out.println("hello");
        return new ResponseEntity<List<CategoryDTO>>(service.addAllTheCategories(categories), HttpStatus.CREATED);
    }
}