package com.fullstack2.dotori.userService.Product;

public interface PurchaseService {

	void addPurchase(Long userId, Long productId);
	
	void removePurchase(Long userId, Long productId);
	

	
}
