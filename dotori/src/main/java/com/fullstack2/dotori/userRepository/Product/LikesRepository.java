package com.fullstack2.dotori.userRepository.Product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.Product.LikeEntity;
import com.fullstack2.dotori.userEntity.Product.ProductEntity;

public interface LikesRepository extends JpaRepository<LikeEntity, Long>{

	LikeEntity findByUserAndProductEntity(User user, ProductEntity productEntity);
	
	 Optional<LikeEntity> findByUser_IdAndProductEntity_ProductId(Long userId, Long productId);
	
	 @Query("SELECT le.productEntity "
	 		+ "FROM LikeEntity le "
	 		+ "WHERE le.user.id = :userId")
	 Page<LikeEntity> findProductsByUserId(@Param("userId") Long userId, Pageable pageable);
}
