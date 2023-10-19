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
public class LikeServiceImpl implements LikeService{
	
	@Autowired
	private final LikesRepository likesRepository;
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final ProductRepository productRepository;

	
	@Override
	public void addLike(Long userId, Long productId) {
		
		User user = userRepository.findById(userId).orElseThrow(
				() -> new NoSuchElementException("likeServiceImpl user null@@@@@@@@@"));
		ProductEntity productEntity =productRepository.findById(productId).orElseThrow(
				() -> new NoSuchElementException("likeServiceImpl productId null @@@@@@@@@@@"));
		
		if (user != null && productEntity != null) {
			LikeEntity likeEntity = LikeEntity.toLikeEntity(user, productEntity);
			likesRepository.save(likeEntity);
			
			productEntity.incrementLikeCount();
			productRepository.save(productEntity);
		}
	}

	@Override
	public void removeLike(Long userId, Long productId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(
				() -> new NoSuchElementException("likeServiceImpl user null@@@@@@@@@"));
		ProductEntity productEntity =productRepository.findById(productId).orElseThrow(
				() -> new NoSuchElementException("likeServiceImpl productId null @@@@@@@@@@@"));
		
		if (user != null && productEntity != null) {
			LikeEntity likeEntity = likesRepository.findByUserAndProductEntity(user, productEntity);
			if (likeEntity != null) {
				likesRepository.delete(likeEntity);
				
				productEntity.decrementLikeCount();
				productRepository.save(productEntity);
			}
		}
		
	}

	@Override
	public boolean checkLike(Long userId, Long productId) {
		
		Optional<LikeEntity> likeEntity = likesRepository.findByUser_IdAndProductEntity_ProductId(userId, productId);
		
		//조회된 결과가 존재하는 경우 true
		//조회된 결과가 존재하지 않는 경우 false
		return likeEntity.isPresent();
	}

	
}
