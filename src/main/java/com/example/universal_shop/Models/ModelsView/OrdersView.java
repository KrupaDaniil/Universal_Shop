package com.example.universal_shop.Models.ModelsView;

import com.example.universal_shop.Models.Goods;
import com.example.universal_shop.Models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdersView {
    private String id;
    private User user;
    private Map<Goods, Long> products;
    private long quantity;
    private double price;
    private boolean processed;
    private String orderIdentifier;
}
