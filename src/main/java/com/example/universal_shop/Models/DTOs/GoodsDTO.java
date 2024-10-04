package com.example.universal_shop.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDTO {
    private String productName;
    private double price;
    private String brand;
    private String description;
    private long categoryId;
}
