package com.user.authentication.registerservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="zuul-gateway")
@RibbonClient(name="database-service")
public interface UserRegisterServiceProxy {
	
	@PostMapping("/database-service/add-user")
	public Boolean addUser(@RequestBody User user);
}
