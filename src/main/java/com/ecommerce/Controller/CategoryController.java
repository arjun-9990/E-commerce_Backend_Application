package com.ecommerce.Controller;

import com.ecommerce.Config.AppConstands;
import com.ecommerce.Payload.CategoryDTO;
import com.ecommerce.Payload.CategoryResponse;
import com.ecommerce.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Category APIs",description = "APIs for managing categories")
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {

        this.service = service;
    }

    @Operation(
            summary = "Get all categories",
            description = "Retrieves a paginated and sorted list of categories. Supports pagination and sorting options."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategory(
            @RequestParam(name = "PageNumber", defaultValue = AppConstands.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "PageSize", defaultValue = AppConstands.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstands.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstands.SORT_ORDER, required = false) String sortOrder
    ) {
        return new ResponseEntity<CategoryResponse>(service.getAllCategories(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }


    @Operation(summary = "Create a new category",description = "This API is used to create and store a new category in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201" , description = "Category created successfully"),
            @ApiResponse(responseCode = "400" , description = "Invalid request data" , content = @Content),
            @ApiResponse(responseCode = "500" , description = "Internal server error" , content = @Content)
    })
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(service.addTheCategories(categoryDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete a category",
            description = "This API is used to delete an existing category from the system using its unique category ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory( @Parameter( description = "Unique ID of the category to be deleted", example = "1", required = true) @PathVariable long categoryId) {
        CategoryDTO deletedCategory = service.deleteTheCategory(categoryId);
        return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
    }

    @Operation(
            summary = "Update a category",
            description = "This API is used to update the details of an existing category using its unique category ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@io.swagger.v3.oas.annotations.parameters.RequestBody( description = "Updated category details",  required = true) @Valid @RequestBody CategoryDTO categoryDTO,
                                                      @Parameter( description = "Unique ID of the category to be updated", example = "1", required = true ) @PathVariable("categoryId") Long categoryId) {
        CategoryDTO savedCategoryDTO = service.updateTheCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Create multiple categories",
            description = "This API is used to create and store multiple categories in a single request."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categories created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/admin/addAllCategories")
    public ResponseEntity<List<CategoryDTO>> createAllCategory( @io.swagger.v3.oas.annotations.parameters.RequestBody( description = "List of category details to be created", required = true ) @RequestBody List<CategoryDTO> categories) {
        return new ResponseEntity<List<CategoryDTO>>(service.addAllTheCategories(categories), HttpStatus.CREATED);
    }
}