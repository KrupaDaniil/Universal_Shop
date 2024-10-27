package com.example.universal_shop.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsFilteredDTO {
    private long categoryId;
    private String productName;
    private double priceMin;
    private double priceMax;
    private String brand;
    private List<String> brands;
}
