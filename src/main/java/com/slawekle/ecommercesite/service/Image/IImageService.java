package com.slawekle.ecommercesite.service.Image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.slawekle.ecommercesite.dtos.ImageDto;
import com.slawekle.ecommercesite.model.Image;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImage(Long id);
    void updateImage(MultipartFile file, Long id);
    List<ImageDto> saveImages(long productId, List<MultipartFile> files);}
