package com.example.universal_shop.Models.DTOs;

import com.example.universal_shop.Models.Categories;
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
    private String productName;
    private double price;
    private String brand;
    private String description;
    private long categoryId;
    private List<CategoriesProductDTO> categories;
}
