package com.fullstack2.dotori.userService.Product;

public interface LikeService {

	void addLike(Long userId, Long productId);
	
	void removeLike(Long userId, Long productId);
	
	boolean checkLike(Long userId, Long productId);
	
}
