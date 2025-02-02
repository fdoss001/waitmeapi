package com.waitme.controller;

import java.util.List;
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

import com.waitme.domain.user.WMUser;
import com.waitme.domain.web.RestRequest;
import com.waitme.domain.web.RestResponse;
import com.waitme.exception.NoResultException;
import com.waitme.service.AdminService;
import com.waitme.service.UserService;
import com.waitme.utils.WMLogger;
import com.waitme.domain.user.Module;
import com.waitme.domain.user.Position;

/**
 * Class to control all admin related functions
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-30
 * @since 1.0 2019-05-30
 */
@RestController
@RequestMapping("/manager")
class ManagerController {
	@Autowired
	UserService userService;
	@Autowired
	AdminService adminService;
	
	WMLogger log = new WMLogger(ManagerController.class);
	
	@PostMapping(value="/getAllEmployees", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse getAllEmployees(@RequestBody RestRequest request) throws NoResultException {
		List<WMUser> employees = userService.getAllWMUsersBasic((int) request.getPayload().get("companyId"), (int) request.getPayload().get("locationId"));
		return new RestResponse("ok", Map.of("employees", employees));
	}
	
	@PostMapping(value="/getEmployeeById", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse getEmployeeById(@RequestBody RestRequest request) throws NoResultException {
		WMUser wmUser = userService.getWMUserById((int) request.getPayload().get("companyId"), (int) request.getPayload().get("id"));
		return new RestResponse("ok", Map.of("employee", wmUser));
	}
	
	@PostMapping(value="/getAllModulesForCompany", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse getAllModulesForCompany(@RequestBody RestRequest request) throws NoResultException {
		List<Module> modules = adminService.getAllModules();
		return new RestResponse("ok", Map.of("modules", modules));
	}
	
	@PostMapping(value="/getAllActivePositionsForLocation", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse getAllActivePositionsForLocation(@RequestBody RestRequest request) throws NoResultException {
		List<Position> modules = adminService.getAllPositions((int) request.getPayload().get("companyId"));
		return new RestResponse("ok", Map.of("positions", modules));
	}
	
	@PostMapping(value="/addUpdateEmployee", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse addUpdateEmployee(@RequestBody RestRequest request) throws NoResultException {
//		List<WMUser> employees = userService.getAllWMUsersBasic((int) request.getPayload().get("companyId"), (int) request.getPayload().get("locationId"));
		return new RestResponse("ok", null);
	}
}
