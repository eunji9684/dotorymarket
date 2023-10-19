package com.fullstack2.dotori.userDTO.Product;

import com.fullstack2.dotori.userEntity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

	private Long productId;
	private String ptitle;
	private Long pPrice;
	private String pContent;
	private String pictureUrl;
	private Long viewCount;
	private Boolean purchaseDone;
	private Long likeCount;
	private Long orderId;
	private Long userId;
	
}
