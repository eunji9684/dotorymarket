package com.fullstack2.dotori.userService.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fullstack2.dotori.userDTO.Product.ProductDTO;
import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.Product.LikeEntity;
import com.fullstack2.dotori.userEntity.Product.ProductEntity;

public interface ProductService {
	
	
	
	default ProductDTO entityToDto(ProductEntity productEntity, User user) {
		
		ProductDTO dto = ProductDTO.builder()
									.productId(productEntity.getProductId())
									.ptitle(productEntity.getPtitle())
									.pPrice(productEntity.getPPrice())
									.pContent(productEntity.getPContent())
									.pictureUrl(productEntity.getPictureUrl())
									.viewCount(productEntity.getViewCount())
									.purchaseDone(productEntity.getPurchaseDone())
									.likeCount(productEntity.getLikeCount())
									.orderId(productEntity.getOrderId())
									//.user(user.getUid())
									.userId(user.getId())
									
									.build();
		
		return dto;
	}
	default ProductDTO entityToDto(ProductEntity productEntity) {
		
		ProductDTO dto = ProductDTO.builder()
				.productId(productEntity.getProductId())
				.ptitle(productEntity.getPtitle())
				.pPrice(productEntity.getPPrice())
				.pContent(productEntity.getPContent())
				.pictureUrl(productEntity.getPictureUrl())
				.viewCount(productEntity.getViewCount())
				.purchaseDone(productEntity.getPurchaseDone())
				.likeCount(productEntity.getLikeCount())
				.orderId(productEntity.getOrderId())
				//.user(productEntity.getUser())
				.userId(productEntity.getUser().getId())
				.build();
		
		return dto;
	}
	
	default ProductEntity dtoToEntity(ProductDTO productDTO) {
		
		//User user = User.builder().uid(productDTO.getUser()).build();
		User user = User.builder().id(productDTO.getUserId()).build();
		ProductEntity productEntity = ProductEntity.builder()
													.ptitle(productDTO.getPtitle())
													.pPrice(productDTO.getPPrice())
													.pContent(productDTO.getPContent())
													.pictureUrl(productDTO.getPictureUrl())
													.viewCount(productDTO.getViewCount())
													.purchaseDone(productDTO.getPurchaseDone())
													.likeCount(productDTO.getLikeCount())
													.orderId(productDTO.getOrderId())
													.user(user)
													.build();
		
		return productEntity;
	}
	

	Page<ProductEntity> ProductSearchList(String searchKeyword, Pageable pageable);	
	
	Page<LikeEntity> ProductLikeList(Long userId, Pageable pageable);
	
	
}
