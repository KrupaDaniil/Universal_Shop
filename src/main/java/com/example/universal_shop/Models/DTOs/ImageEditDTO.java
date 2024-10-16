package com.example.universal_shop.Models.DTOs;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageEditDTO {
    private long imdId;
    private String imageName;
    private Boolean isMainImage;
}
