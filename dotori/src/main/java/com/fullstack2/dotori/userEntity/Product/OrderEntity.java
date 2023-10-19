package com.fullstack2.dotori.userEntity.Product;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.fullstack2.dotori.userEntity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;


@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@Table(name = "Orders")
public class OrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	
	@Column(name = "purchase_done", nullable = false)
	@ColumnDefault("false")
	private Boolean purchaseDone;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	private User user;
	
	@ManyToOne(targetEntity = ProductEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private ProductEntity productEntity;
	
	@Builder
	public OrderEntity(Boolean purchaseDone, User user,
			ProductEntity productEntity) {
		
		this.purchaseDone = purchaseDone;
		this.user = user;
		this.productEntity =productEntity;
	}
}
