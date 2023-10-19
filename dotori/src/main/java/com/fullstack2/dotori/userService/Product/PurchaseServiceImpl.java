package com.fullstack2.dotori.userService.Product;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.Product.LikeEntity;
import com.fullstack2.dotori.userEntity.Product.ProductEntity;
import com.fullstack2.dotori.userRepository.UserRepository;
import com.fullstack2.dotori.userRepository.Product.LikesRepository;
import com.fullstack2.dotori.userRepository.Product.ProductRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService{
	
	@Autowired
	private final LikesRepository likesRepository;
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final ProductRepository productRepository;



	@Override
	public void addPurchase(Long userId, Long productId) {
		
		User user = userRepository.findById(userId).orElseThrow(
				() -> new NoSuchElementException("likeServiceImpl user null@@@@@@@@@"));
		ProductEntity productEntity =productRepository.findById(productId).orElseThrow(
				() -> new NoSuchElementException("likeServiceImpl productId null @@@@@@@@@@@"));
		
		if (user != null && productEntity != null) {
			
			productEntity.setPurchaseDone(true);
			productRepository.save(productEntity);
		}
	}

	@Override
	public void removePurchase(Long userId, Long productId) {
		
		User user = userRepository.findById(userId).orElseThrow(
				() -> new NoSuchElementException("likeServiceImpl user null@@@@@@@@@"));
		ProductEntity productEntity =productRepository.findById(productId).orElseThrow(
				() -> new NoSuchElementException("likeServiceImpl productId null @@@@@@@@@@@"));
		
		if (user != null && productEntity != null) {
			
			productEntity.setPurchaseDone(false);
			productRepository.save(productEntity);
		}
		
	}

	
}
