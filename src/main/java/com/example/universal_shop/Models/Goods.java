package com.example.universal_shop.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String productName;
    private double price;
    private String brand;
    private String description;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Images> images;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categories_id")
    private Categories categories;
}
