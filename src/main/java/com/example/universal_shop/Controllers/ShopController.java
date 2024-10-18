package com.example.universal_shop.Controllers;

import com.example.universal_shop.Models.Categories;
import com.example.universal_shop.Services.CategoriesService;
import com.example.universal_shop.Services.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShopController {
    private final CategoriesService categoriesService;
    private final GoodsService goodsService;

    public ShopController(CategoriesService categoriesService, GoodsService goodsService) {
        this.categoriesService = categoriesService;
        this.goodsService = goodsService;
    }

    @GetMapping("/categories")
    public String showCategories(Model model) {
        List<Categories> categories  = categoriesService.findAll();
        model.addAttribute("categories", categories);

        return "categories";
    }

}
