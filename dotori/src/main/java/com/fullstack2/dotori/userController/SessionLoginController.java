package com.fullstack2.dotori.userController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fullstack2.dotori.userDTO.JoinRequest;
import com.fullstack2.dotori.userDTO.LoginRequest;
import com.fullstack2.dotori.userDTO.UserDTO;
import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.Product.LikeEntity;
import com.fullstack2.dotori.userRepository.UserRepository;
import com.fullstack2.dotori.userRepository.Product.LikesRepository;
import com.fullstack2.dotori.userService.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SessionLoginController {

	private final UserService userService;
	private final UserRepository userRepository;
	private final LikesRepository likesRepository;

	// @SessionAttribute : 세션에 데이터를 저장하거나 세션에서 데이터를 읽어오는 데 사용.
	/*
	 * @GetMapping(value = {"","/"}) public String home(Model
	 * model, @SessionAttribute(name = "userId", required = false) Long userId) {
	 * 
	 * model.addAttribute("loginType", "user"); model.addAttribute("pageName",
	 * "세션을 통한로그인 홈");
	 * 
	 * User loginUser = userService.getLoginUserById(userId);
	 * 
	 * if (loginUser != null) { model.addAttribute("id", loginUser.getId()); }
	 * 
	 * return "home"; }
	 */

	@GetMapping("/sign_up")
	public String signupPage(Model model) {
		model.addAttribute("loginType", "user");
		model.addAttribute("pageName", "세션을 통한 회원가입");

		model.addAttribute("joinRequest", new JoinRequest());

		return "sign_up";
	}

	@PostMapping("/sign_up")
	// @valid : 데이터 유효성 검사를 수행 검증 위반의 경우 예외발생
	// @bindingResult : @valid 유효성 검사를 수행한 결과, 검증 오류 메세지나 오류상세정보를 저장
	// @ModelAttribute : 스프링 MVC에서 컨트롤러 메서드의 파라미터와 모델 객체 간의 데이터 전달을 도와주는 어노테이션
	public String signupPage(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {
		model.addAttribute("loginType", "user");
		model.addAttribute("pageName", "세션을 통한 회원가입post");

		// id 중복체크 + 빈칸체크
		if (userService.checkUidDuplicate(joinRequest.getUid())) {
			bindingResult.addError(new FieldError("joinRequest", "uid", "이미 가입된 아이디입니다."));
		}

		// upw == upwcheck 체크하기
		if (!joinRequest.getUpw().equals(joinRequest.getUpwcheck())) {
			bindingResult.addError(new FieldError("joinRequest", "upwcheck", "비밀번호가 일치하지않습니다."));
		}
		// 이메일 중복체크
		if (userService.checkUemailDuplicate(joinRequest.getUemail())) {
			bindingResult.addError(new FieldError("joinRequest", "uemail", "이미 가입된 이메일입니다."));
		}

		if (bindingResult.hasErrors()) {
			return "sign_up";
		}

		userService.join(joinRequest);

		return "redirect:/user/login";
	}

	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("loginType", "user");
		model.addAttribute("pageName", "세션을 통한 로그인기능");

		model.addAttribute("loginRequest", new LoginRequest());
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult,
			HttpServletRequest httpServletRequest, Model model) {
		model.addAttribute("loginType", "user");
		model.addAttribute("pageName", "세션을 통한 로그인 post");

		User user = userService.login(loginRequest);

		// 로그인 아이디나 비밀번호가 틀린 경우
		if (user == null) {
			bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
		}

		if (bindingResult.hasErrors()) {
			return "login";
		}

		// 로그인 성공시 세션생성
		// 1. 기존의 세션파기 2.세션이 없으면 세션 생성 3. 세션에 userId를 넣어줌
		httpServletRequest.getSession().invalidate();
		HttpSession session = httpServletRequest.getSession(true);

		session.setAttribute("user", user);
		session.setMaxInactiveInterval(1800); // 세션 유지시간 30분

		return "redirect:/user/home";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.invalidate();

		return "redirect:/user/home";
	}

	@GetMapping("/home")
	public String home(HttpSession session, Model model) {

		if (session.getAttribute("user") != null) {
			/*
			Long userId = ((User) session.getAttribute("user")).getId();
			UserRole userRole = ((User) session.getAttribute("user")).getRole();
			String uname = ((User) session.getAttribute("user")).getUname();

			model.addAttribute("loginType", userRole);
			model.addAttribute("uname", uname);
			*/
			//정석 추가
			User user = (User) session.getAttribute("user");
			model.addAttribute("user", user);
			
			String myUid = ((User) session.getAttribute("user")).getUid();
			UserDTO userDTO = userService.getLoginUserByUid(myUid);
			
			if (userDTO.getUaddress() == null) {
				model.addAttribute("addressMessage", "주소를 입력해주세요.");
				
				return "redirect:/user/info"; 
			}
		}

		return "/home";
	}

	@GetMapping("/info")
	public String userInfo(HttpSession session, Model model) {

		if (session.getAttribute("user") == null) {
			return "redirect:/user/login";
		}

		String myUid = ((User) session.getAttribute("user")).getUid();

		UserDTO userDTO = userService.getLoginUserByUid(myUid);

		model.addAttribute("sessionUser", userDTO);
		model.addAttribute("user", userDTO);

		return "updateUser";
	}

	@ResponseBody
	@PostMapping(value = "/info", produces = "text/html; charset=UTF-8")
	public String update(@ModelAttribute UserDTO userDTO) {

		String message = "";
		System.out.println(userDTO.getId());

		userService.modifyUser(userDTO);

		message = "<script>alert('수정 완료 되었습니다.');" + "location.href='/user/info';" + "</script>";
		return message;

	}

	@GetMapping("/likelist")
	public String likesList(HttpSession session, Model model, Pageable pageable
								,Long userId){
	
		if(session.getAttribute("user") == null) {
			return "redirect:/user/login";
		}
		
		 User user = (User)session.getAttribute("user");
		 
		 
		 userId = user.getId();
		 model.addAttribute("user", user);
		 
		 int pageSize = 16;
		 int pageNumber = pageable.getPageNumber();
		 pageable = PageRequest.of(pageNumber, pageSize);//, Sort.by("Product_id").descending());
		 
		 Page<LikeEntity> products = null;
		 products = likesRepository.findProductsByUserId(userId, pageable);
		System.out.println(products);
		System.out.println(products.isEmpty());
		System.out.println(products.hasContent());
		 model.addAttribute("products", products);
		 
		 return "/likelist";
	}



	// 비밀번호 변경
	@GetMapping("/modifyPassword")
	public String modifyPassword(HttpSession session, Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/user/login";
		}

		String myUid = ((User) session.getAttribute("user")).getUid();

		UserDTO userDTO = userService.getLoginUserByUid(myUid);

		model.addAttribute("sessionUser", userDTO);

		return "/modifyPassword";
	}

	@ResponseBody
	@PostMapping(value = "/modifyPassword", produces = "text/html; charset=UTF-8")
	public String modifyPasswordDo(UserDTO userDTO, String newUpw, String newUpwCheck, String nowUpw, Model model,
			HttpSession session) {

		Optional<User> searchId = userRepository.findById(userDTO.getId());
		User user = searchId.get();

		if (!nowUpw.equals(user.getUpw())) {
			return "<script>" + "alert('비밀번호가 틀립니다.');" + "location.href = '/user/modifyPassword'" + "</script>";

		}

		if (!newUpw.equals(newUpwCheck)) {
			return "<script>" + "alert('비밀번호가 일치하지 않습니다.');" + "location.href = '/user/modifyPassword'" + "</script>";
		}

		userService.modifyPassword(userDTO.getId(), newUpw);

		session.invalidate();
		return "<script>" + "alert('비밀번호가 변경되었습니다 재로그인해주세요');" + "location.href = '/user/login'" + "</script>";
	}

	@GetMapping("/findPassword")
	public String findPasswordPage(Model model) {
	    model.addAttribute("loginType", "user");
	    model.addAttribute("pageName", "비밀번호 찾기"); // 페이지 제목 등을 설정할 수 있음

	    return "findPassword"; // 비밀번호 찾기 페이지의 Thymeleaf 템플릿 이름
	}

	@PostMapping("/findPassword")
	public String doFindPassword(@RequestParam("uemail") String userEmail) {
	    // 이메일을 사용하여 비밀번호 찾는 로직을 작성하고,
	    // 비밀번호를 재설정하거나 임시 비밀번호를 생성하는 등의 작업을 수행합니다.
	    
	    // 이후 사용자에게 알맞은 응답을 반환하거나 리다이렉트를 수행합니다.

	    return "redirect:/user/login"; // 비밀번호 찾기 후 로그인 페이지로 리다이렉트하는 예시
	}


	@GetMapping("/admin")
	public String adminPage(HttpSession session, Model model) {
		if (session.getAttribute("user") == null) {
			return "redirect:/user/login";
		}

		User user = (User) session.getAttribute("user");
		model.addAttribute("loginType", user.getRole());
		model.addAttribute("pageName", "세션 어드민 페이지");

		/*
		 * if (!loginUser.getRole().equals(UserRole.ADMIN)) { return "redirect:/user"; }
		 */
		return "admin";
	}

    @GetMapping("/find_username")
    public String findUsernamePage(Model model) {
        model.addAttribute("loginType", "user");
        model.addAttribute("pageName", "아이디 찾기"); // 페이지 제목 설정
        return "find_username"; // 아이디 찾기 페이지의 Thymeleaf 템플릿 이름
    }

    @PostMapping("/find_username")
    public String findUsername(@RequestParam("uname") String uname, @RequestParam("uemail") String uemail, Model model) {
        // uname과 uemail을 사용하여 데이터베이스에서 사용자 정보를 조회합니다.
        User user = userRepository.findByUnameAndUemail(uname, uemail);

        if (user != null) {
            // 사용자 정보에서 아이디를 가져와서 Thymeleaf 템플릿으로 전달합니다.
            String foundUsername = user.getUid();
            model.addAttribute("foundUsername", foundUsername);
        } else {
            // 사용자를 찾지 못한 경우
            model.addAttribute("foundUsername", null);
        }

        return "find_username_result"; // 결과를 표시할 Thymeleaf 템플릿 이름
    }

}
