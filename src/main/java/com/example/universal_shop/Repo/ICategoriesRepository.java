package com.example.universal_shop.Repo;

import com.example.universal_shop.Models.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoriesRepository extends JpaRepository<Categories, Long> {
}
