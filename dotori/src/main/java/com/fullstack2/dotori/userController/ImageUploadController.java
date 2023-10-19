package com.fullstack2.dotori.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack2.dotori.userDTO.Product.ProductDTO;
import com.fullstack2.dotori.userEntity.Product.ProductEntity;
import com.fullstack2.dotori.userRepository.Product.ProductRepository;
import com.fullstack2.dotori.userService.ImageUploadService;
import com.fullstack2.dotori.userService.Product.ProductService;

import java.io.IOException;
import java.util.UUID;

@Controller
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService; // ProductService는 상품 정보 및 이미지 URL 저장을 처리하는 서비스 클래스입니다.
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    
    
    @PostMapping("/uploadImage")
    public String handleFileUpload(@RequestParam("imageFile") MultipartFile file, 
                                   @RequestParam("productId") Long productId,
                                   Model model) throws IOException {
       
    	if (!file.isEmpty()) {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            // 이미지를 저장하고 파일 경로(또는 URL)을 DB에 저장하는 로직을 imageUploadService에서 처리합니다.
            String pictureUrl = imageUploadService.saveProductThumbnailImage(file, uniqueFileName);
            
            //imageUrl 추가 or 수정해서 DB에 넣기
            ProductEntity productEntity = productRepository.getReferenceById(productId);
            ProductDTO productDTO = productService.entityToDto(productEntity);
            productDTO.setPictureUrl(pictureUrl);
            productEntity = productService.dtoToEntity(productDTO);
            
            productRepository.saveImage(pictureUrl, productId);
            System.out.println("url" + pictureUrl);
        }
        
    	
        // 이미지와 함께 상품 ID도 URL 매개변수로 전달
        return "redirect:/productDetail?productId=" + productId;
    }

    
}
