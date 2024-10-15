package com.example.universal_shop.Services;

import com.example.universal_shop.Models.Categories;
import com.example.universal_shop.Models.DTOs.GoodsDTO;
import com.example.universal_shop.Models.DTOs.ProductEditDTO;
import com.example.universal_shop.Models.Goods;
import com.example.universal_shop.Repo.ICategoriesRepository;
import com.example.universal_shop.Repo.IGoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    private final IGoodsRepository goodsRepository;
    private final ICategoriesRepository categoriesRepository;

    @Autowired
    public GoodsService(IGoodsRepository goodsRepository, ICategoriesRepository categoriesRepository) {
        this.goodsRepository = goodsRepository;
        this.categoriesRepository = categoriesRepository;
    }

    public void saveGoods(GoodsDTO goodsDto) {
        Categories ct = categoriesRepository.findById(goodsDto.getCategoryId()).orElse(null);

        if (ct == null) {
            throw new IllegalArgumentException("Invalid category id");
        }

        Goods goods = new Goods();
        goods.setProductName(goodsDto.getProductName());
        goods.setPrice(goodsDto.getPrice());
        goods.setBrand(goodsDto.getBrand());
        goods.setDescription(goodsDto.getDescription());
        goods.setCategories(ct);
        goodsRepository.save(goods);
    }

    public List<Goods> findAll() {
        return goodsRepository.findAll().stream().toList();
    }

    public Goods findById(long id) {
        return goodsRepository.findById(id).orElse(null);
    }

    public void delete(long id) {
        goodsRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return goodsRepository.existsById(id);
    }
}
