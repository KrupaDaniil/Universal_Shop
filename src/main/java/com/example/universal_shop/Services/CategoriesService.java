package com.example.universal_shop.Services;

import com.example.universal_shop.Models.Categories;
import com.example.universal_shop.Repo.ICategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriesService {
    private final ICategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesService(ICategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public Categories saveCategories(Categories categories) {
        return categoriesRepository.save(categories);
    }

    public Iterable<Categories> findAll() {
        return categoriesRepository.findAll();
    }

    public Optional<Categories> findById(long id) {
        return categoriesRepository.findById(id);
    }

    public void delete(long id) {
        categoriesRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return categoriesRepository.existsById(id);
    }
}
