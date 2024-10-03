package com.example.universal_shop.Controllers;

import com.example.universal_shop.Models.Categories;
import com.example.universal_shop.Models.Goods;
import com.example.universal_shop.Models.Images;
import com.example.universal_shop.Services.CategoriesService;
import com.example.universal_shop.Services.GoodsService;
import com.example.universal_shop.Services.ImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class AdminController {
    private final CategoriesService categoriesService;
    private final GoodsService goodsService;
    private final ImagesService imagesService;

    @Autowired
    public AdminController(CategoriesService categoriesService, GoodsService goodsService, ImagesService imagesService) {
        this.categoriesService = categoriesService;
        this.goodsService = goodsService;
        this.imagesService = imagesService;
    }

    @GetMapping("/product-management")
    public String productManagement(Model model) {
        Iterable<Categories> ct = categoriesService.findAll();
        Iterable<Goods> gds = goodsService.findAll();
        Iterable<Images> img = imagesService.findAll();

        ArrayList<Categories> categories = new ArrayList<>();
        ArrayList<Goods> goods = new ArrayList<>();
        ArrayList<Images> images = new ArrayList<>();

        ct.forEach(categories::add);
        gds.forEach(goods::add);
        img.forEach(images::add);

        model.addAttribute("categories", categories);
        model.addAttribute("goods", goods);
        model.addAttribute("images", images);

        return "admin-panel/product-management";
    }
}
