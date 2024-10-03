package com.example.universal_shop.Repo;

import com.example.universal_shop.Models.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGoodsRepository extends JpaRepository<Goods, Long> {
}
