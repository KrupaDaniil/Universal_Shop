package com.example.universal_shop.Models.ModelsView;

import com.example.universal_shop.Models.Goods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImagesView {
    private long id;
    private String imageName;
    private boolean isMainImage;
    private Goods goods;
}
