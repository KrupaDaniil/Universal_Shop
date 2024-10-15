package com.example.universal_shop.Repo;

import com.example.universal_shop.Models.Categories;
import com.example.universal_shop.Models.DTOs.CategoriesProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Queue;

public interface ICategoriesRepository extends JpaRepository<Categories, Long> {
    @Query("select new com.example.universal_shop.Models.DTOs.CategoriesProductDTO(ct.id, ct.categoryName) from Categories ct")
    List<CategoriesProductDTO> findCategoriesByIdAndCategoryName();
}
