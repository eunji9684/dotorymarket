package com.fullstack2.dotori;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing//자동시간처리를 위해서는 반드시 main에 이거 추가하세요.
public class Dotori1Application {

	public static void main(String[] args) {
		SpringApplication.run(Dotori1Application.class, args);
	}

}
