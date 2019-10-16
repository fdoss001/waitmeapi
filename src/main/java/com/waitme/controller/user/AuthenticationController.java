package com.waitme.controller.user;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waitme.domain.user.WMUser;
import com.waitme.domain.web.RestRequest;
import com.waitme.domain.web.RestResponse;
import com.waitme.exception.AuthenticationException;
import com.waitme.exception.NoUserException;
import com.waitme.service.UserService;

/**
 * Class to represent an rest response to the client
 * Every controller handling an rest call should wrap its reponse in this class
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-30
 * @since 1.0 2019-05-30
 */
@RestController
@RequestMapping("/authentication")
class AuthenticationController {
	@Autowired
	UserService userService;
	
	Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	
	@PostMapping(value="/login")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse login(@RequestBody RestRequest request) throws NoUserException, AuthenticationException, IOException {
//		RestRequest req = new ObjectMapper().readValue(request.getReader(), RestRequest.class);
		String userName = (String) request.getPayload().get("userName");
		String password = (String) request.getPayload().get("password");
		
//		WMUser wmUser = userService.getWMUser(userName);
//		userService.validateCredentials(wmUser, password);
		
		WMUser wmUser = new WMUser(1000000002, "fdoss", "Fernando", "Dos Santos");
		wmUser.setIconPath("https://cdn2.iconfinder.com/data/icons/user-icon-2-1/100/user_5-15-512.png");
		
		return new RestResponse("ok", Map.of("user", wmUser));
	}
}
