package com.waitme.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.waitme.domain.user.WMUser;
import com.waitme.domain.web.RestRequest;
import com.waitme.domain.web.RestResponse;
import com.waitme.exception.AuthenticationException;
import com.waitme.exception.NoResultException;
import com.waitme.service.UserService;
import com.waitme.utils.WMLogger;

/**
 * Class to control all authentication
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-30
 * @since 1.0 2019-05-30
 */
@RestController
@RequestMapping("/authentication")
class AuthenticationController {
	@Autowired
	UserService userService;
	
	WMLogger log = new WMLogger(AuthenticationController.class);
	
	@PostMapping(value="/login")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse login(@RequestBody RestRequest request) throws NoResultException, AuthenticationException {
		String userName = (String) request.getPayload().get("userName");
		String password = (String) request.getPayload().get("password");
		
		WMUser wmUser = userService.getSimpleWMUser(userName);
		wmUser = userService.loginWMUser(wmUser, password);
		
		return new RestResponse("ok", Map.of("user", wmUser));
	}
}
