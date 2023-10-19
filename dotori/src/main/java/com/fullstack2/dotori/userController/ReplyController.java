package com.fullstack2.dotori.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.Product.ProductEntity;
import com.fullstack2.dotori.userEntity.Product.ReplyEntity;
import com.fullstack2.dotori.userRepository.Product.ReplyRepository;
import com.fullstack2.dotori.userService.Product.ReplyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReplyController {

	@Autowired
	ReplyService replyService;
	@Autowired
	ReplyRepository replyRepository;
	
	
	
	//수정
	@PostMapping("/editReply")
	public ResponseEntity<String> editReply(HttpServletRequest request, HttpSession session,
										HttpServletResponse response) {
		
		
		Long reId = Long.parseLong(request.getParameter("reId"));
		System.out.println(request.getParameter("reId")+"댓글id");
		String reContent = request.getParameter("reContent");
		System.out.println(request.getParameter("reContent")+"컨텐츠");
		replyService.editReply(reId, reContent);

		
		return ResponseEntity.ok("{\"success\": true}");
		
	}
	
	
	//삭제
	@PostMapping("/deleteReply")
	public ResponseEntity<String> deleteReply(HttpServletRequest request, HttpSession session,
								HttpServletResponse response) {
		
		Long reId = Long.parseLong(request.getParameter("reId"));
		
		replyService.deleteReply(reId);
		
		return ResponseEntity.ok("{\"success\": true}");
	}
	
	
	//댓글등록
	@PostMapping("/registerReply")
	public ResponseEntity<String> registerReply (HttpServletRequest request, HttpSession session,
								HttpServletResponse response) {
		
		//productDetail.html에서 request에 담아서 보낸 정보 추출 
		
		//상품 번호
		Long productId = Long.parseLong(request.getParameter("productId"));
		//유저 번호
		Long id = Long.parseLong(request.getParameter("id")); 
		//댓글 내용
		String reContent = request.getParameter("reContent");
		
		
		if (reContent.isBlank()) {
			
			return ResponseEntity.ok("{\"success\": false}");
		}	
		
		ProductEntity product = ProductEntity.builder()
											.productId(productId)
											.build();
		
		User user = User.builder()
						.id(id)
						.build();
		
		//부모가 없는 글
		ReplyEntity reply = ReplyEntity.builder()
										.parent(null)
										.reContent(reContent)
										.user(user)
										.productEntity(product)
										.build();
		
		replyRepository.save(reply);
		
		
		return ResponseEntity.ok("{\"success\": true}");
	}
	
	
	//답글 달기
	@PostMapping("/replyOnReply")
	public ResponseEntity<String> replyOnReply (HttpServletRequest request, HttpSession session,
								HttpServletResponse response) {
		
		//productDetail.html에서 request에 담아서 보낸 정보 추출 
		
		//상품 번호
		Long productId = Long.parseLong(request.getParameter("productId"));
		//유저 번호
		Long id = Long.parseLong(request.getParameter("id")); 
		//부모 댓글 번호
		Long parentReId = Long.parseLong(request.getParameter("reId"));
		//댓글 내용
		String reContent = request.getParameter("reContent");
		
		
		
		//부모 댓글 만들기
		ReplyEntity parentReply = ReplyEntity.builder()
											 .reId(parentReId)
											 .build();
		

		if (reContent.isBlank()) {
			
			return ResponseEntity.ok("{\"success\": false}");
		}	
		
		ProductEntity product = ProductEntity.builder()
											.productId(productId)
											.build();
		
		User user = User.builder()
						.id(id)
						.build();
		
		
		//부모가 있는 글
		ReplyEntity childReply = ReplyEntity.builder()
										.parent(parentReply)
										.reContent(reContent)
										.user(user)
										.productEntity(product)
										.build();
		
		replyRepository.save(childReply);
		
		
		return ResponseEntity.ok("{\"success\": true}");
	}
	
	
	
}
