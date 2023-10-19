package com.fullstack2.dotori.userEntity.Product;


import com.fullstack2.dotori.userEntity.BaseEntity;
import com.fullstack2.dotori.userEntity.User;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.FetchType;

@Setter
@Getter
@Entity
@Table(name = "Likes")
public class LikeEntity extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "like_id")
	private Long likeId;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	private User user;
	
	@ManyToOne(targetEntity = ProductEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private ProductEntity productEntity;
	
	
	public static LikeEntity toLikeEntity(User user, ProductEntity productEntity) {
		LikeEntity likeEntity = new LikeEntity();
		likeEntity.setUser(user);
		likeEntity.setProductEntity(productEntity);
		
		return likeEntity;
	}
}
