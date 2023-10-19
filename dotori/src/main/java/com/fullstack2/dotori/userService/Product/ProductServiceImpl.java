package com.fullstack2.dotori.userService.Product;


import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fullstack2.dotori.userEntity.Product.LikeEntity;
import com.fullstack2.dotori.userEntity.Product.ProductEntity;
import com.fullstack2.dotori.userRepository.Product.LikesRepository;
import com.fullstack2.dotori.userRepository.Product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductRepository productRepository;
	@Autowired
	LikesRepository likesRepository;
	
	@Override
	public Page<ProductEntity> ProductSearchList(String searchKeyword, Pageable pageable) {
		//return productRepository.findByPtitleContaining(searchKeyword, pageable);
		return productRepository.findByPtitleContainingOrUser_UaddressContaining(searchKeyword, searchKeyword, pageable);
	}

	@Override
	public Page<LikeEntity> ProductLikeList(Long userId, Pageable pageable) {
		return likesRepository.findProductsByUserId(userId,pageable);
	}
	
	 public void togglePurchaseDone(Long productId, Long userId) {
	        ProductEntity productEntity = productRepository.findById(productId)
	                .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다."));

	        // 사용자 ID와 상품의 소유자 ID를 비교하여 확인
	        if (productEntity.getUser().getId().equals(userId)) {
	            boolean currentPurchaseDone = productEntity.isPurchaseDone();
	            productEntity.setPurchaseDone(!currentPurchaseDone);
	            productRepository.save(productEntity);
	        } else {
	            throw new IllegalStateException("상품을 수정할 권한이 없습니다.");
	        }
	    }

}
