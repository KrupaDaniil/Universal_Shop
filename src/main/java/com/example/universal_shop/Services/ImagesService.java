package com.example.universal_shop.Services;

import com.example.universal_shop.Models.Images;
import com.example.universal_shop.Repo.IImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImagesService {
    private final IImagesRepository imagesRepository;

    @Autowired
    public ImagesService(IImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

    public Images save(Images images) {
        return imagesRepository.save(images);
    }

    public Iterable<Images> findAll() {
        return imagesRepository.findAll();
    }

    public Optional<Images> findById(long id) {
        return imagesRepository.findById(id);
    }

    public void delete(long id) {
        imagesRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return imagesRepository.existsById(id);
    }
}
