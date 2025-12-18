package com.ecommerce.Controller;

import com.ecommerce.Config.AppConstands;
import com.ecommerce.Payload.ProductDTO;
import com.ecommerce.Payload.ProductResponse;
import com.ecommerce.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Product APIs", description = "APIs for managing products")
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Add a new product",
            description = "Creates a new product under a specific category. Admin access required."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@io.swagger.v3.oas.annotations.parameters.RequestBody( description = "Product details to be added", required = true ) @Valid @RequestBody ProductDTO productDTO,
                                                 @Parameter( description = "Category ID under which product is created", example = "1", required = true ) @PathVariable Long categoryId) {

        ProductDTO savedProductDTO = productService.addProduct(categoryId, productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
    }

    @Operation(
            summary = "Get all products",
            description = "Retrieves all products with pagination and sorting support."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @Parameter(description = "Page number (starts from 0)", example = "0")
            @RequestParam(name = "PageNumber", defaultValue = AppConstands.PAGE_NUMBER, required = false)
            Integer pageNumber,

            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(name = "PageSize", defaultValue = AppConstands.PAGE_SIZE, required = false)
            Integer pageSize,

            @Parameter(description = "Field name for sorting", example = "productName")
            @RequestParam(name = "sortBy", defaultValue = AppConstands.SORT_PRODUCTS_BY, required = false)
            String sortBy,

            @Parameter(description = "Sorting order (asc or desc)", example = "asc")
            @RequestParam(name = "sortOrder", defaultValue = AppConstands.SORT_ORDER, required = false)
            String sortOrder
    ) {
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @Operation(
            summary = "Get products by category",
            description = "Retrieves products belonging to a specific category with pagination and sorting."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/public/categories/{categoryId}/product")
    public ResponseEntity<ProductResponse> getProductByCategory(@Parameter(description = "Category ID", example = "1", required = true) @PathVariable Long categoryId,
                                                                @RequestParam(name = "PageNumber", defaultValue = AppConstands.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "PageSize", defaultValue = AppConstands.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstands.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstands.SORT_ORDER, required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @Operation(
            summary = "Search products by keyword",
            description = "Searches products using a keyword with pagination and sorting."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Products found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/public/product/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword( @Parameter(description = "Keyword to search products", example = "mobile", required = true) @PathVariable String keyword,
                                                               @RequestParam(name = "PageNumber", defaultValue = AppConstands.PAGE_NUMBER, required = false) Integer pageNumber,
                                                               @RequestParam(name = "PageSize", defaultValue = AppConstands.PAGE_SIZE, required = false) Integer pageSize,
                                                               @RequestParam(name = "sortBy", defaultValue = AppConstands.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                               @RequestParam(name = "sortOrder", defaultValue = AppConstands.SORT_ORDER, required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.serachProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.FOUND).body(productResponse);
    }

    @Operation(
            summary = "Update a product",
            description = "Updates product details using product ID. Admin access required."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO,
                                                    @Parameter(description = "Product ID to be updated", example = "101", required = true) @PathVariable Long productId) {
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProductDTO);
    }

    @Operation(
            summary = "Delete a product",
            description = "Deletes a product using product ID. Admin access required."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@Parameter(description = "Product ID to be deleted", example = "101", required = true) @PathVariable Long productId) {
        ProductDTO deleteProductDTO = productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(deleteProductDTO);
    }

    @Operation(
            summary = "Update product image",
            description = "Uploads or updates the image of a product."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product image updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid image file"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage( @Parameter(description = "Product ID", example = "101", required = true) @PathVariable Long productId,
                                                          @Parameter(description = "Product image file", required = true) @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

}
