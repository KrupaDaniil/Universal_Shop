package com.example.universal_shop.Models.DTOs;

import com.example.universal_shop.Models.Categories;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEditDTO {
    private long id;
    private String productName;
    private double price;
    private String brand;
    private String description;
    private Long categoryId;
    private List<CategoriesProductDTO> categories;
}
