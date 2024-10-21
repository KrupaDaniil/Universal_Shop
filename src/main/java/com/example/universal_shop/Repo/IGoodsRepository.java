package com.example.universal_shop.Repo;

import com.example.universal_shop.Models.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IGoodsRepository extends JpaRepository<Goods, Long> {
    @Query("select gs from Goods gs where gs.categories.id = :ctId")
    List<Goods> findByCategoriesAndId(@Param("ctId") long ctId);
}
