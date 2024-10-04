package com.example.universal_shop.Repo;

import com.example.universal_shop.Models.Images;
import com.example.universal_shop.Models.ModelsView.ImagesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IImagesRepository extends JpaRepository<Images, Long> {
    @Query("select new com.example.universal_shop.Models.ModelsView.ImagesView(im.id, im.imageName, im.isMainImage, im.goods)  from Images im ")
    List<ImagesView> findByIdAndImageNameAndMainImage();
}
