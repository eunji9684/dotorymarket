package com.fullstack2.dotori.userDTO.Product;

import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.Product.ProductEntity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ProductRequestDTO {

	@NotEmpty(message = "제목을 입력해주세요.")
	private String ptitle;
	@NotNull(message = "가격을 입력해주세요.")
	private Long pPrice;
	@NotEmpty(message = "내용을 입력해주세요.")
	private String pContent;
	private String pictureUrl;
	private User user;
	
	@Builder
	public ProductRequestDTO(String ptitle, Long pPrice, String pContent, 
			String pictureUrl,User user) {
		this.ptitle = ptitle;
		this.pPrice = pPrice;
		this.pContent = pContent;
		this.pictureUrl = pictureUrl;
		this.user = user;
	}
	
	public ProductEntity toEntity() {
		return ProductEntity.builder()
				.ptitle(ptitle)
				.pPrice(pPrice)
				.pContent(pContent)
				.pictureUrl(pictureUrl)
				.user(user)
				.build();
			
	}
}
