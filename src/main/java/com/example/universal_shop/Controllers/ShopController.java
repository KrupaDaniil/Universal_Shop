package com.example.universal_shop.Controllers;

import com.example.universal_shop.Models.Categories;
import com.example.universal_shop.Models.Goods;
import com.example.universal_shop.Models.Images;
import com.example.universal_shop.Models.User;
import com.example.universal_shop.Services.CategoriesService;
import com.example.universal_shop.Services.GoodsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Controller
public class ShopController {
    private final CategoriesService categoriesService;
    private final GoodsService goodsService;

    public ShopController(CategoriesService categoriesService, GoodsService goodsService) {
        this.categoriesService = categoriesService;
        this.goodsService = goodsService;
    }

    @GetMapping("/categories")
    public String showCategories(@AuthenticationPrincipal User user, Model model) {
        List<Categories> categories  = categoriesService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("user", user);

        return "categories";
    }

    @GetMapping("/categories/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        byte[] image = categoriesService.findById(id).getImage();

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().header("Content-Type", "image/jpg").body(image);
    }

    @GetMapping("/goods/{ctId}")
    public String showGoods(@PathVariable long ctId, @AuthenticationPrincipal User user, Model model) {
        List<Goods> goods = goodsService.findGoodsByCategoryId(ctId);
        model.addAttribute("goods", goods);
        model.addAttribute("user", user);
        return "goods";
    }

    @GetMapping("/product/details/{id}")
    public String showDetails(@PathVariable long id, @AuthenticationPrincipal User user, Model model) {
        Goods goods = goodsService.findById(id);
        model.addAttribute("product", goods);
        model.addAttribute("user", user);
        return "details";
    }

    @GetMapping("/product/images/{id}")
    public ResponseEntity<List<String>> getProductImages(@PathVariable long id) {

        List<Images> images = goodsService.findById(id).getImages();

        if (images == null || images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<String> imageList = new ArrayList<>();

        for (Images image : images) {
            String cnVl = Base64.getEncoder().encodeToString(image.getImage());
            imageList.add(cnVl);
        }

        return ResponseEntity.ok().body(imageList);
    }

    @GetMapping("/product/image/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable long id) {
        byte[] image;

        try {
            image = Objects.requireNonNull(goodsService.findById(id).getImages().stream()
                    .filter(Images::getIsMainImage).findFirst().orElse(null)).getImage();

            return ResponseEntity.ok().header("Content-Type", "image/jpg").body(image);
        } catch (NullPointerException ex) {
            return ResponseEntity.notFound().build();
        }


    }


}
