package com.fullstack2.dotori.userService.Product;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.Product.ReplyEntity;
import com.fullstack2.dotori.userRepository.Product.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	ReplyRepository replyRepository;
	

	@Override
	public void editReply(Long reId, String reContent) {
		
		 ReplyEntity reply = replyRepository.findById(reId).orElseThrow(
				 	() -> new NoSuchElementException("modifyServiceImpl user null@@@@@@@@@"));

		 reply.setReContent(reContent);
		 
		 replyRepository.save(reply);
		 //수정은 버튼을 눌렀을 때, 수정 창이 뜨고, 그 내용을 받아와야 하겠구나.
	}

	@Override
	public void deleteReply(Long reId) {

		ReplyEntity reply = replyRepository.findById(reId).orElseThrow(
			 	() -> new NoSuchElementException("modifyServiceImpl user null@@@@@@@@@"));
		
		replyRepository.delete(reply);
	}

}



