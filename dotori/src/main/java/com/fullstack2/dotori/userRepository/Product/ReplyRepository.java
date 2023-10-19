package com.fullstack2.dotori.userRepository.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack2.dotori.userEntity.Product.LikeEntity;
import com.fullstack2.dotori.userEntity.Product.ReplyEntity;
import com.fullstack2.dotori.userEntity.Product.ProductEntity;


@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long>{

	
	List<ReplyEntity> findAllReplyEntitiesByProductEntityAndParentIsNull(ProductEntity productEntity);
}
