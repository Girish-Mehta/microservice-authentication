package com.user.authentication.loginservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@EnableFeignClients("com.user.authentication.loginservice")
public class LoginServiceApplication {


	@Autowired
	private UserRegisterServiceProxy proxy;
	
	public static void main(String[] args) {
		SpringApplication.run(LoginServiceApplication.class, args);
	}
	
	@PostMapping("/login")
	public Boolean loginUser(@RequestBody User user) {
		Boolean response = proxy.verifyUser(user);
		return response;
	}
}
