package com.fullstack2.dotori.userDTO;

import java.time.LocalDateTime;

import com.fullstack2.dotori.userEntity.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

	
	private Long id;
	private LocalDateTime moddate;
	private LocalDateTime regdate;
	private Enum role;
	private String uaddress;
	private String uemail;
	@NotEmpty(message = "아이디를 입력하세요")
	private String uid;
	private String uname;
	@NotEmpty(message = "비밀번호를 입력하세요")
	private String upw;
	private Boolean usocial;	
	
	//UserEntity 객체를 입력받아 UserDTO 객체로 변환하는 기능을 수행
	public static UserDTO toUserDTO(User user){
		UserDTO userDTO = new UserDTO();
		
		userDTO.setId(user.getId());
		userDTO.setModdate(user.getModDate());
		userDTO.setRegdate(user.getRegdate());
		userDTO.setUaddress(user.getUaddress());
		userDTO.setUemail(user.getUemail());
		userDTO.setUid(user.getUid());
		userDTO.setUname(user.getUname());
		userDTO.setUpw(user.getUpw());
		userDTO.setRole(user.getRole());
		userDTO.setUsocial(user.getUsocial());
		
		return userDTO;
	}
}
