package com.user.authentication.databaseservice;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {

	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/add-user")
	public Boolean addUser(@RequestBody User user) {
		Optional<User> option = userRepository.findById(user.getUsername());

		if(option.isPresent()) {
			if(option.get().getPassword().equals(user.getPassword())) {
				return false;
			} 
		}
		
		userRepository.save(user);
		return true;
	}
	
	@PostMapping("/verify-user")
	public Boolean verifyUser(User user) {
		Optional<User> option = userRepository.findById(user.getUsername());

		if(option.isPresent()) {
			if(option.get().getPassword().equals(user.getPassword())) {
				return true;
			} 
		}

		return false;
	}	
}
