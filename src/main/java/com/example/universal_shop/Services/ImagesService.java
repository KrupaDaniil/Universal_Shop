package com.example.universal_shop.Services;

import com.example.universal_shop.Models.DTOs.ImageEditDTO;
import com.example.universal_shop.Models.DTOs.ImagesDTO;
import com.example.universal_shop.Models.Goods;
import com.example.universal_shop.Models.Images;
import com.example.universal_shop.Models.ModelsView.ImagesView;
import com.example.universal_shop.Repo.IGoodsRepository;
import com.example.universal_shop.Repo.IImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ImagesService {
    private final IImagesRepository imagesRepository;
    private final IGoodsRepository goodsRepository;

    @Autowired
    public ImagesService(IImagesRepository imagesRepository, IGoodsRepository goodsRepository) {
        this.imagesRepository = imagesRepository;
        this.goodsRepository = goodsRepository;
    }

    public void save(ImagesDTO imagesDTO) throws IOException {
        Goods goods = goodsRepository.findById(imagesDTO.getGoodsId()).orElse(null);

        if (goods == null) {
            throw new IllegalArgumentException("Product with id " + imagesDTO.getGoodsId() + " not found");
        }

        Images img = new Images();
        img.setImageName(imagesDTO.getImageName());
        img.setImage(imagesDTO.getImage().getBytes());
        img.setMainImage(imagesDTO.getIsMainImage());
        img.setGoods(goods);

        imagesRepository.save(img);
    }

    public List<Images> findAll() {
        return imagesRepository.findAll().stream().toList();
    }

    public List<ImagesView> findAllView() {
        return imagesRepository.findByIdAndImageNameAndMainImage();
    }

    public Images findById(long id) {
        return imagesRepository.findById(id).orElse(null);
    }

    public void delete(long id) {
        imagesRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return imagesRepository.existsById(id);
    }

    public int updateByImage(ImageEditDTO imageEditDTO) {
        return imagesRepository.updateByImageNameAndMainImage(imageEditDTO);
    }
}
