package com.example.universal_shop.Models.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasketDTO implements Serializable {
    private List<ProductBasketDTO> goodsList;
    private long count;
    private double totalPrice;

    public BasketDTO(List<ProductBasketDTO> goodsList) {
        this.goodsList = goodsList;
    }

    public void updateObject(ProductBasketDTO productBasketDTO) {
    }
}
