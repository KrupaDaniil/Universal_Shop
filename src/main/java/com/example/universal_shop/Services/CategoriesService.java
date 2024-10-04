package com.example.universal_shop.Services;

import com.example.universal_shop.Models.Categories;
import com.example.universal_shop.Models.DTOs.CategoriesDTO;
import com.example.universal_shop.Repo.ICategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriesService {
    private final ICategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesService(ICategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public void saveCategories(CategoriesDTO categoriesDTO) throws IOException {
        Categories categories = new Categories();
        categories.setCategoryName(categoriesDTO.getCategoryName());
        categories.setImage(categoriesDTO.getImage().getBytes());
        categoriesRepository.save(categories);
    }

    public List<Categories> findAll() {
        return categoriesRepository.findAll().stream().toList();
    }

    public Categories findById(long id) {
        return categoriesRepository.findById(id).orElse(null);
    }

    public void delete(long id) {
        categoriesRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return categoriesRepository.existsById(id);
    }
}
