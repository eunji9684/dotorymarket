package com.fullstack2.dotori;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userRepository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@SpringBootTest
class Dotori1ApplicationTests {

	@Autowired
	UserRepository userRepository;
	
	//@Test
	void sessionTest(HttpServletRequest request) {
		 // HttpServletRequest를 통해 세션 객체 얻기
        HttpSession session = request.getSession();
        session.setAttribute("mySessionAttribute", "세션테스트");
        // 세션에서 데이터 읽기
        String sessionData = (String) session.getAttribute("mySessionAttribute");
        System.out.println(sessionData);
	}
	
	
	
	
	
	//@Test
	void queryTest() {
		System.out.println(userRepository.getUserByUId("js"));
		System.out.println("비번확인 : " +((User)userRepository.getUserByUId("js")).getUpw());
		System.out.println("회원가입된 아이디인가요?(존재)" + userRepository.existsByUid("js"));
		System.out.println("회원가입된 아이디인가요?(없음)" + userRepository.existsByUid("js999"));
		
	}
	
	
	//@Test
	void contextLoads() {
		System.out.println("hi");
	}

}
