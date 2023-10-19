package com.fullstack2.dotori.userController;

import java.util.HashMap;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.UserRole;
import com.fullstack2.dotori.userRepository.UserRepository;
import com.fullstack2.dotori.userService.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class KakaoContorller {

	private final UserService userService;
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/user/kakaoLogin")
	public String login(@RequestParam("code") String code, HttpServletRequest httpServletRequest) {
		System.out.println("컨트롤러 코드 : " + code);

		String access_Token = userService.getAccessToken(code);
		System.out.println("컨트롤러 access 토큰 : " + access_Token);

		HashMap<String, Object> userInfo = userService.getUserInfo(access_Token);
		System.out.println("컨트롤러 유저정보가져오기 : " + userInfo);
		
		if (userInfo == null || userInfo.get("uemail") == null) {
			System.out.println("##########################################");
			return "redirect:/user/login";
		
		}
		httpServletRequest.getSession().invalidate();
		HttpSession session = httpServletRequest.getSession(true);
		
		System.out.println("요기@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		System.out.println("요기@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		System.out.println("요기@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		
		User user = new User().builder()
							.uemail((String)userInfo.get("uemail"))
							.uid((String)userInfo.get("uemail"))
							.uname((String)userInfo.get("nickname"))
							.usocial(true)
							.role(UserRole.USER)
							.build();
		if(!userRepository.findByUid((String)userInfo.get("uemail")).isPresent()) {	
			
			userRepository.save(user);
			
		}
		
		user = userRepository.findByUid((String)userInfo.get("uemail"))
				.orElseThrow(() -> new NoSuchElementException("카카오 유저 이메일 가져오는거 에러"));;
		
		
		//UserRole userRole = ((User)session.getAttribute("user")).getRole();
		
//		user.setUemail((String)userInfo.get("uemail"));
//		user.setUid((String)userInfo.get("nickname"));
//		user.setUsocial(true);
		//user.setRole(userRole.USER);
		
		session.setAttribute("user", user);
		session.setMaxInactiveInterval(1800);
		
		System.out.println("유저값이 오나?" + session.getAttribute("user"));
		
		
		return "redirect:/user/home";	
	}
	
	
}
