package com.fullstack2.dotori.userController;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.Product.LikeEntity;
import com.fullstack2.dotori.userService.Product.LikeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LikeController {
	
	@Autowired
	private final LikeService likeService;
	
	@PostMapping("/addLike")
	public ResponseEntity<String> addLike(HttpServletRequest request, HttpSession session,
									HttpServletResponse response) {
		
		Long user = ((User)session.getAttribute("user")).getId();
		
		Long productId = Long.parseLong(request.getParameter("productId"));
		
		if (user == null) {
			
			return ResponseEntity.ok("{\"success\": false}");
		}		
		
		likeService.addLike(user, productId);
		
		return ResponseEntity.ok("{\"success\": true}");
}
	
	@PostMapping("/removeLike")
	public ResponseEntity<String> removeLike(HttpServletRequest request, HttpSession session,
										HttpServletResponse response) {
		
		Long user = ((User)session.getAttribute("user")).getId();
		Long productId = Long.parseLong(request.getParameter("productId"));
		
		if (user == null) {
			return ResponseEntity.ok("{\"success\": false}");
		}
		
		likeService.removeLike(user, productId);
		
		return ResponseEntity.ok("{\"success\": true}");
	}
	

}
