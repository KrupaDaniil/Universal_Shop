package com.example.universal_shop.Repo;

import com.example.universal_shop.Models.DTOs.ImageEditDTO;
import com.example.universal_shop.Models.Images;
import com.example.universal_shop.Models.ModelsView.ImagesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IImagesRepository extends JpaRepository<Images, Long> {
    @Query("select new com.example.universal_shop.Models.ModelsView.ImagesView(im.id, im.imageName, im.isMainImage, im.goods)  from Images im ")
    List<ImagesView> findByIdAndImageNameAndMainImage();

    @Modifying
    @Query("update Images img set img.imageName = :#{#imgEdt.imageName}, img.isMainImage = :#{#imgEdt.isMainImage} where img.id = :#{#imgEdt.imdId}")
    int updateByImageNameAndMainImage(@Param("imgEdt")ImageEditDTO imageEditDTO);
}
