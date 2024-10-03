package com.example.universal_shop.Services;

import com.example.universal_shop.Models.Goods;
import com.example.universal_shop.Repo.IGoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoodsService {
    private final IGoodsRepository goodsRepository;

    @Autowired
    public GoodsService(IGoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    public Goods saveGoods(Goods goods) {
        return goodsRepository.save(goods);
    }

    public Iterable<Goods> findAll() {
        return goodsRepository.findAll();
    }

    public Optional<Goods> findById(long id) {
        return goodsRepository.findById(id);
    }

    public void delete(long id) {
        goodsRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return goodsRepository.existsById(id);
    }
}
