package com.fullstack2.dotori.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

	private String uid;
	private String upw;
	
}
