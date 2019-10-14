package com.waitme.controller.user;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.waitme.domain.user.WMUser;
import com.waitme.domain.web.RestRequest;
import com.waitme.domain.web.RestResponse;
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
@RequestMapping("/user")
class UserController {
	@Autowired
	UserService userService;
	
	@PostMapping(value="/getUser", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse getUser(@RequestBody RestRequest request) throws NoUserException, JsonParseException, JsonMappingException, IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		RestRequest request = mapper.readValue(body, RestRequest.class);
		WMUser wmUser = userService.getWMUser((String) request.getPayload().get("userName"));
		
		return new RestResponse("SUCCESS", Map.of("wmUser", wmUser));
	}
}
