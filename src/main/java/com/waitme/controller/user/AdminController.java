package com.waitme.controller.user;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.waitme.domain.user.WMUser;
import com.waitme.domain.web.RestRequest;
import com.waitme.domain.web.RestResponse;
import com.waitme.exception.NoResultException;
import com.waitme.service.UserService;

/**
 * Class to represent an rest response to the client
 * Every controller handling an rest call should wrap its reponse in this class
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-30
 * @since 1.0 2019-05-30
 */
@RestController
@RequestMapping("/admin")
class AdminController {
	@Autowired
	UserService userService;
	
	Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@PostMapping(value="/getAllEmployees", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse getAllEmployees(@RequestBody RestRequest request) throws NoResultException {
		List<WMUser> employees = userService.getAllWMUsersBasic((int) request.getPayload().get("companyId"), (int) request.getPayload().get("locationId"));
		return new RestResponse("ok", Map.of("employees", employees));
	}
}
