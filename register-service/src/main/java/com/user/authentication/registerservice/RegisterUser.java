package com.user.authentication.registerservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegisterUser {
	
	@Autowired
	private UserRegisterServiceProxy proxy;
	
	@PostMapping("/users")
	public Boolean registerUser(@RequestBody User user) {
		Boolean response = proxy.addUser(user);
		
		return response;
	}
}
