package com.fullstack2.dotori.userEntity;

import org.hibernate.annotations.DynamicUpdate;

import com.fullstack2.dotori.userDTO.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Setter
public class User extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String uid;
	
	private String uname;
	
	private String upw;
	
	@Column(unique = true)
	private String uemail;
	
	private String uaddress;
	
	private boolean usocial;
	
	private UserRole role;
	
	public boolean getUsocial() {
		return this.usocial;
	}
	
	//수정가능한 부분만 setter
	public void changeUpw(String upw) {
		this.upw = upw;
	}
	public void changeUemail(String uemail) {
		this.uemail = uemail;
	}
	public void changeUaddress(String uaddress) {
		this.uaddress = uaddress;
	}
	
	public static User toUserEntity(UserDTO userDTO) {
		
		
		
		User user = User.builder()
						.id(userDTO.getId())
						.uaddress(userDTO.getUaddress())
						.uemail(userDTO.getUemail())
						.uid(userDTO.getUid())
						.uname(userDTO.getUname())
						.upw(userDTO.getUpw())
						//.role(UserRole.USER)
						.role((UserRole)userDTO.getRole())
						.build();
		
		
		
		return user;
	}
	
}
