package com.ecommerce.Payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "CategoryDTO",
        description = "DTO representing product category details"
)
public class CategoryDTO {
    @Schema(
            description = "Unique identifier of the category",
            example = "1"
    )
    private Long categoryId;

    @NotNull(message = "Category can't be null")
    @Size(min = 5, message = "Category name must contain at least 5 characters")
    @Schema(
            description = "Name of the category",
            example = "Electronics",
            minLength = 5,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String categoryName;
}
