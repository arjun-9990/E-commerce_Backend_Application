package com.ecommerce.Payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long categoryId;

    @NotNull(message = "Category can't be null")
    @Size(min = 5, message = "Category name must contain at least 5 characters")
    private String categoryName;
}
