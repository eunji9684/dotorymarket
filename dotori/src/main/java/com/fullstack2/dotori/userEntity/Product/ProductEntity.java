package com.fullstack2.dotori.userEntity.Product;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.beans.factory.annotation.Value;

import com.fullstack2.dotori.userEntity.BaseEntity;
import com.fullstack2.dotori.userEntity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GenerationType;

@Setter
@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@Table(name = "Product")
public class ProductEntity extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id", nullable = false)
	private Long productId;
	
	@Column(name = "p_title", nullable = false)
	private String ptitle;
	
	@Column(name = "p_price", nullable = false)
	private Long pPrice;
	
	@Column(name = "p_content")
	@ColumnDefault("NULL")
	private String pContent;
	
	
	@Column(name = "picture_url")
	private String pictureUrl;
	
	@Column(name = "view_count")
	@ColumnDefault("0")
	private Long viewCount;
	
	@Column(name = "purchase_done")
	@ColumnDefault("false")
	private Boolean purchaseDone;
	
	@Column(name = "like_count")
	@ColumnDefault("0")
	private Long likeCount;
	
	@Column(name = "order_id")
	private Long orderId;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	private User user;
	
	@OneToMany(cascade = CascadeType.REMOVE)
	private List<ReplyEntity>replyList;
	
	
	@Builder()
	public ProductEntity(Long productId,String ptitle, Long pPrice, String pContent, String pictureUrl,
			Long viewCount, Boolean purchaseDone, Long likeCount, Long orderId, User user) {
		
		this.productId =productId;
		this.ptitle = ptitle;
		this.pPrice = pPrice;
		this.pContent = pContent;
		this.pictureUrl = pictureUrl;
		this.viewCount = viewCount;
		this.purchaseDone = purchaseDone;
		this.likeCount = likeCount;
		this.orderId = orderId;
		this.user = user;
		
	}
	
	
	//디폴트 이미지 넣기
	@PrePersist
    public void defaultImage() {
        if (pictureUrl == null) {
        	//pictureUrl = "디폴트이미지.jpg";
        	pictureUrl = "ffc4439c-3fda-4060-8fdf-417589bd3598_디폴트이미지.jpg";
        }
    }
	
	public void incrementLikeCount() {
		likeCount++;
	}
	public void decrementLikeCount() {
		likeCount--;
	}
	
	
	public void incrementViewCount() {
		viewCount++;
	}
	
	 public boolean isPurchaseDone() {
	        return purchaseDone;
	    }
	
}
