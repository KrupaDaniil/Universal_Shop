package com.example.universal_shop.Models.ModelsView;

import com.example.universal_shop.Models.Goods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOrdersView {
    private Map<Goods, Long> products;
    private long quantity;
    private double price;
    private boolean processed;
    private boolean canceledUser;
    private String orderIdentifier;
}
