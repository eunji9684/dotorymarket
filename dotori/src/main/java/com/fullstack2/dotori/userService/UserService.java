package com.fullstack2.dotori.userService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack2.dotori.userDTO.JoinRequest;
import com.fullstack2.dotori.userDTO.LoginRequest;
import com.fullstack2.dotori.userDTO.UserDTO;
import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userRepository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional //트랜잭션의 범위를 정의하고 작업중 문제 발생시 롤백 기능 제공
@RequiredArgsConstructor
public class UserService {

	@Autowired
	private final UserRepository userRepository;
	
	//uid 중복체크( 중복시 true)
	public boolean checkUidDuplicate(String uid) {
		return userRepository.existsByUid(uid);
	}
	
	//uemail 중복채크 ( 중복시 true )
	public boolean checkUemailDuplicate(String uemail) {
		return userRepository.existsByUemail(uemail);
	}
	
	//회원가입 
	//화면에서 uid, uname, uemail, upw, uaddress 를 입력받아서 User 로 진행
	//중복체크는 controller에서 
	public void join(JoinRequest req) {
		userRepository.save(req.toEntity());
	}
	
	//로그인
	//화면에서 uid , upw 입력받아서
	//DB 정보와 일치하면 --> User return;
	//DB 정보와 일치하지 않으면 --> null return;
	
	public User login(LoginRequest req) {
		Optional<User> optionalUser = userRepository.findByUid(req.getUid());
		
		if (optionalUser.isEmpty()) {
			return null;
		}
		
		User user = optionalUser.get();
		
		//아이디와 비밀번호가 일치하지 않는 경우에도 null return
		
		if (!user.getUpw().equals(req.getUpw())) {
			return null;
		}
		
		return user;
	}
	
	//ID(uid x)를 입력받아 user return
	// 인증, 인가시 필요
	//로그인을 하지 않았거나, id 번호가 존재하지 않으면 null
	public User getLoginUserById(Long id) {
		if(id == null) {
			return null;
		}
		
		Optional<User> optionalUser = userRepository.findById(id);
		
		if (optionalUser.isEmpty()) {
			return null;
		}
		
		return optionalUser.get();
	}
	
	//uid를 입력받아 user return
	//인증, 인가시 필요
	//로그인을 하지 않았거나, id 번호가 존재하지 않으면 null
	public UserDTO getLoginUserByUid(String myUid) {
		if (myUid == null) {
			return null;
		}
		
		Optional<User> optionalUser = userRepository.findByUid(myUid);
		
		if (optionalUser.isEmpty()) {
			return null;
		}
		
		return UserDTO.toUserDTO(optionalUser.get()); 
	}
	
	//회원정보(주소) 수정
	public void modifyUser(UserDTO userDTO) {
		
		User user = userRepository.findById(userDTO.getId())
								.orElseThrow(() -> new NoSuchElementException());
		
		//주소변경	
		user.setUaddress(userDTO.getUaddress());
		
		
		//userRepository.save(User.toUserEntity(userDTO));
		userRepository.save(user);
		
	}
	
	public void modifyPassword(Long id, String newUpw) {
		
		Optional<User> optional = userRepository.findById(id);
		
		if (optional.isPresent()) {
			User user = optional.get();
			user.setUpw(newUpw);
			userRepository.save(user);
		}else {
			throw new RuntimeException("가입되어있지 않는 회원입니다.");
		}
	}

	
	//카카오 로그인 시작
	public String getAccessToken(String auth_code) {
		String access_token = "";
		String refresh_token = "";
		String reqURL = "https://kauth.kakao.com/oauth/token";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("grant_type=authorization_code");
			stringBuilder.append("&client_id=9dd282ac92f1791f4d6f8b50fb17c7e6");
			stringBuilder.append("&redirect_uri=http://localhost:90/user/kakaoLogin");
			stringBuilder.append("&code=" + auth_code);
			bufferedWriter.write(stringBuilder.toString());
			bufferedWriter.flush();
			
			int responseCode = conn.getResponseCode();
			System.out.println("리스폰 코드 : " + responseCode);
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String result = "";
			
			while ((line = bufferedReader.readLine()) != null) {
				result += line;
			}
			
			JsonParser jsonParser = new JsonParser();
			JsonElement jsonElement = jsonParser.parse(result);
			
			access_token = jsonElement.getAsJsonObject().get("access_token").getAsString();
			refresh_token = jsonElement.getAsJsonObject().get("refresh_token").getAsString();
			
			System.out.println("access_token = " + access_token);
			System.out.println("refresh_token = " + refresh_token);
			
			bufferedReader.close();
			bufferedWriter.close();
			
		} catch (Exception e) {
				e.printStackTrace();
		}
		return access_token;
	}
	
	
	
	
	
	
	
	
	public HashMap<String, Object> getUserInfo(String access_token){
		HashMap<String, Object> userInfo = new HashMap<>();
		String reqURL = "https://kapi.kakao.com/v2/user/me";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + access_token);
			int responseCode = conn.getResponseCode();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line = "";
			String result = "";
			while ((line = bufferedReader.readLine()) != null ) {
				result += line;
			}
			JsonParser jsonParser = new JsonParser();
			JsonElement jsonElement = jsonParser.parse(result);
			
			JsonObject properties = jsonElement.getAsJsonObject().get("properties").getAsJsonObject();
			JsonObject kakao_account = jsonElement.getAsJsonObject().get("kakao_account").getAsJsonObject();
			
			String nickname = properties.getAsJsonObject().get("nickname").getAsString();
			String email = kakao_account.getAsJsonObject().get("email").getAsString();
			
			userInfo.put("nickname", nickname);
			userInfo.put("uemail", email);
			bufferedReader.close();
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return userInfo;
	}
	
}
