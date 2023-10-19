package com.fullstack2.dotori.userRepository.Product;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fullstack2.dotori.userDTO.Product.ProductDTO;
import com.fullstack2.dotori.userEntity.Product.ProductEntity;

import jakarta.transaction.Transactional;



@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>{

	 ProductEntity save(ProductDTO productDTO);
	 
	 /*
	 @Modifying
	 @Transactional
	 @Query(value = "UPDATE product p set p.picture_url = :picture_url where p.product_id = :product_id")
	 void saveImage(@Param("picture_url") String pictureUrl, @Param("product_id") Long productId);
	 */
	 
	 //쿼리에는 엔티티명을 써야함!
	 @Modifying
	 @Transactional
	 @Query(value = "UPDATE ProductEntity p set p.pictureUrl = :picture_url where p.productId = :product_id")
	 void saveImage(@Param("picture_url") String pictureUrl, @Param("product_id") Long productId);
	 
	 Page<ProductEntity> findByPtitleContaining(String searchKeyword, Pageable pageable);

	 /*
	 @Query(value = "SELECT u.productEntity"
	 		+ "FROM User u"
	 		+ "WHERE u.user.uaddress = :uaddress")
	 */
	 
	  Page<ProductEntity> findByPtitleContainingOrUser_UaddressContaining(String titleKeyword, String addressKeyword, Pageable pageable);
	 
}
