package com.fullstack2.dotori.userDTO;

import org.hibernate.validator.constraints.Length;

import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

	@NotEmpty(message = "ID를 입력해 주세요.")
	private String uid;
	
	@NotEmpty(message = "이름을 입력해 주세요.")
	private String uname;
	
	@NotEmpty(message = "이메일을 입력해 주세요.")
	@Email(message = "이메일 형식이 틀렷습니다.")
	private String uemail;
	
	@NotEmpty(message = "비밀번호를 입력해 주세요.")
	@Length(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하로 입력해주세요.")
	private String upw;
	private String upwcheck;
	
	@NotEmpty(message = "주소를 입력해 주세요.")
	private String uaddress;
	
	private boolean usocial;
	
	private UserRole role;
	
	//비밀번호를 암호화 하지 않은 메서드
	
	public User toEntity() {
		return User.builder()
				.uid(this.uid)
				.uname(this.uname)
				.uemail(this.uemail)
				.upw(this.upw)
				.uaddress(this.uaddress)
				.usocial(this.usocial)
				.role(UserRole.USER)
				.build();
	}
	
//	//비밀번호를 암호화 하는 메서드
//	public User toEntity(String encodedPassword) {
//		return User.builder()
//				.uid(this.uid)
//				.uname(this.uname)
//				.uemail(this.uemail)
//				.upw(encodedPassword)
//				.uaddress(this.uaddress)
//				.usocial(this.usocial)
//				.build();
//	}
	
}
