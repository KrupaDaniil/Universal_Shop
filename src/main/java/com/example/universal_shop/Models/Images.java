package com.example.universal_shop.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private byte[] image;
    private String imageName;
    private boolean isMainImage;

    @ManyToOne
    @JoinColumn(name = "goods_id")
    private Goods goods;

}
