package com.example.universal_shop.Services;


import com.example.universal_shop.Models.DTOs.GoodsDTO;
import com.example.universal_shop.Models.Goods;
import com.example.universal_shop.Repo.ICategoriesRepository;
import com.example.universal_shop.Repo.IGoodsRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void saveGoods(GoodsDTO goodsDto, @Nullable Long id) {
        Goods goods;

        if (id == null) {
           goods = new Goods();
        }
        else {
            goods = goodsRepository.findById(id).orElse(null);
        }

        if (goods != null) {
            if (goodsDto.getProductName() != null) {
                goods.setProductName(goodsDto.getProductName());
            }

            goods.setPrice(goodsDto.getPrice());

            if (goodsDto.getBrand() != null) {
                goods.setBrand(goodsDto.getBrand());
            }
            if (goodsDto.getDescription() != null) {
                goods.setDescription(goodsDto.getDescription());
            }
            goods.setCategories(categoriesRepository.findById(goodsDto.getCategoryId()).orElse(null));

            goodsRepository.save(goods);

        }
        else {
            throw new IllegalArgumentException("Invalid goods id");
        }
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

    public void setCategoriesToNull(long id) {
        List<Goods> goodsList = findAll();

        if (goodsList != null) {
            goodsList.stream().filter(r -> r.getCategories().getId() == id).forEach(r -> r.setCategories(null));

            goodsRepository.saveAll(goodsList);
        }
    }

    public List<Goods> findGoodsByCategoryId(long ctId) {
        return goodsRepository.findByCategoriesAndId(ctId);
    }
}
