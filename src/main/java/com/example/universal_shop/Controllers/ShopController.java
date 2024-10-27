package com.example.universal_shop.Controllers;

import com.example.universal_shop.Models.*;
import com.example.universal_shop.Models.DTOs.GoodsFilteredDTO;
import com.example.universal_shop.Services.CategoriesService;
import com.example.universal_shop.Services.GoodsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class ShopController {
    private final CategoriesService categoriesService;
    private final GoodsService goodsService;
    private List<Images> images;

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
        List<String> ct = goods.stream().map(Goods::getBrand).distinct().toList();
        GoodsFilteredDTO dto = new GoodsFilteredDTO();
        dto.setBrands(ct);
        dto.setPriceMin(goods.stream().map(Goods::getPrice).min(Double::compareTo).orElse(0.0));
        dto.setPriceMax(goods.stream().map(Goods::getPrice).reduce(0.0, Double::max));
        model.addAttribute("goods", goods);
        model.addAttribute("user", user);
        model.addAttribute("goods_f", dto);
        model.addAttribute("ctId", ctId);
        return "goods";
    }

    @PostMapping("/goods/filtered")
    public String showFilteredGoods(@ModelAttribute("goodsFiltered")GoodsFilteredDTO goodsFilteredDTO, @AuthenticationPrincipal User user, Model model) {
        List<Goods> goods = goodsService.findGoodsByCategoryId(goodsFilteredDTO.getCategoryId());

        if (goods != null) {
            List<String> ct = goods.stream().map(Goods::getBrand).distinct().toList();
            goodsFilteredDTO.setBrands(ct);

            if (goodsFilteredDTO.getBrand() != null && !goodsFilteredDTO.getBrand().isBlank()) {
                goods = goods.stream().filter(r -> r.getBrand().equals(goodsFilteredDTO.getBrand())).toList();
            }

            if (goodsFilteredDTO.getPriceMin() >= 0 && goodsFilteredDTO.getPriceMax() > 0) {
                goods = goods.stream().filter(r -> r.getPrice() >= goodsFilteredDTO.getPriceMin() && r.getPrice() <= goodsFilteredDTO.getPriceMax()).toList();
            }

            if (goodsFilteredDTO.getProductName() != null && !goodsFilteredDTO.getProductName().isBlank()) {
                goods = goods.stream().filter(r -> r.getProductName().contains(goodsFilteredDTO.getProductName())).toList();
            }
        }

        model.addAttribute("goods", goods);
        model.addAttribute("goods_f", goodsFilteredDTO);
        model.addAttribute("ctId", goodsFilteredDTO.getCategoryId());
        model.addAttribute("user", user);

        return "goods";
    }

    @GetMapping("/product/details/{id}")
    public String showDetails(@PathVariable long id, @AuthenticationPrincipal User user, Model model) {
        Goods goods = goodsService.findById(id);
        if (goods == null) {
            throw new IllegalArgumentException("Product with id" + id + "is missing");
        }

        images = goodsService.findById(id).getImages();

        model.addAttribute("product", goods);
        model.addAttribute("user", user);
        return "details";
    }

    @GetMapping("/product/images/{id}")
    public ResponseEntity<byte[]> getProductImages(@PathVariable long id) {

        if (images == null || images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] img = images.stream().filter(m -> m.getId() == id).findFirst().orElseThrow().getImage();
            return ResponseEntity.ok().header("Content-Type", "image/jpg").body(img);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/product/image/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable long id) {
        try {
            byte[] image = Objects.requireNonNull(goodsService.findById(id).getImages().stream()
                    .filter(Images::getIsMainImage).findFirst().orElse(null)).getImage();

            return ResponseEntity.ok().header("Content-Type", "image/jpg").body(image);
        } catch (NullPointerException ex) {
            return ResponseEntity.notFound().build();
        }
    }


}
