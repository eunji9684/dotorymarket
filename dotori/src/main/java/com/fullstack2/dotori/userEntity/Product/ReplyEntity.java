package com.fullstack2.dotori.userEntity.Product;

import java.util.ArrayList;
import java.util.List;

import com.fullstack2.dotori.userEntity.BaseEntity;
import com.fullstack2.dotori.userEntity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReplyEntity extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reId;
	
	private String reContent;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private ProductEntity productEntity;
	
	@ManyToOne
	private ReplyEntity parent;
	
	@OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
	private List<ReplyEntity> children = new ArrayList<ReplyEntity>();

	/*
	public ReplyEntity(Long reId, List<ReplyEntity> children) {
		this.reId = reId;
		this.children = children;
		for(ReplyEntity child : children) {
			child.parent = this;
		}
	}
	*/
}
