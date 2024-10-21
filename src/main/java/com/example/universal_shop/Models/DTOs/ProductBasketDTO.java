package com.example.universal_shop.Models.DTOs;

import com.example.universal_shop.Models.Goods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductBasketDTO implements Serializable {
    private Goods goods;
    private long quantity;
    private double finalPrice;

    public ProductBasketDTO(Goods goods, long quantity) {
        this.goods = goods;
        this.quantity = quantity;
    }
}
