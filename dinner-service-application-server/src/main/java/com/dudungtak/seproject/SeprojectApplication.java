package com.dudungtak.seproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// TODO : 재고 관리, 로깅, 리팩토링
@SpringBootApplication
public class SeprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeprojectApplication.class, args);
	}

}
