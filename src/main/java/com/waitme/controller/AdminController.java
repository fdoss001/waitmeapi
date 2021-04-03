package com.waitme.controller;

import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.waitme.domain.web.RestRequest;
import com.waitme.domain.web.RestResponse;
import com.waitme.exception.NoResultException;
import com.waitme.service.RestaurantManagementService;
import com.waitme.domain.item.ItemOption;
import com.waitme.domain.item.Menu;
import com.waitme.domain.user.WMUser;

/**
 * Class to control all admin related functions
 * @author Fernando Dos Santos
 * @version 1.0 2020-01-29
 * @since 1.0 2020-01-29
 */
@RestController
@RequestMapping("/admin")
class AdminController {
	@Autowired
	RestaurantManagementService restaurantManagementService;
	
	Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@GetMapping(value="/getAllMenus", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse getAllMenus(@PathParam("companyId") int companyId, @PathParam("locationId") int locationId) throws NoResultException {
		List<Menu> menus = restaurantManagementService.getAllMenusMinimal(companyId, locationId);
		return new RestResponse("ok", Map.of("menus", menus));
	}
	
	
	//OPTIONS
	@GetMapping(value="/getAllItemOptions", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse getAllItemOptions(@PathParam("companyId") int companyId) {
		try {
			List<ItemOption> options = restaurantManagementService.getAllItemOptionsMinimal(companyId);
			return new RestResponse("ok", Map.of("options", options));
		} catch(Exception e) {
			return new RestResponse("error", Map.of("errorMsg", e.getMessage()));
		}
	}
	
	@GetMapping(value="/getItemOption", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse getAllItemOptions(@PathParam("companyId") int companyId, @PathParam("optionId") int optionId) {
		try {	
			ItemOption option = restaurantManagementService.getItemOption(companyId, optionId);
			return new RestResponse("ok", Map.of("option", option));
		} catch(Exception e) {
			return new RestResponse("error", Map.of("errorMsg", e.getMessage()));
		}
	}
	
	@PostMapping(value="/createItemOption", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse createItemOption(@RequestBody RestRequest request) {
		try {
			ItemOption option = (ItemOption) request.getPayload().get("option");
			WMUser creator = (WMUser) request.getPayload().get("user");
			restaurantManagementService.createNewOption(option, creator);
			return new RestResponse("ok", Map.of("optionId", option.getId()));
		} catch(Exception e) {
			return new RestResponse("error", Map.of("errorMsg", e.getMessage()));
		}
	}
	
	@PostMapping(value="/updateItemOption", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse updateItemOption(@RequestBody RestRequest request) {
		try {
			ItemOption option = (ItemOption) request.getPayload().get("option");
			WMUser creator = (WMUser) request.getPayload().get("user");
			restaurantManagementService.updateOption(option, creator);
			return new RestResponse("ok", null);
		} catch(Exception e) {
			return new RestResponse("error", Map.of("errorMsg", e.getMessage()));
		}
	}
	
	@PostMapping(value="/toggleActivateItemOption", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse toggleActivateItemOption(@RequestBody RestRequest request) {
		try {
			int companyId = (int) request.getPayload().get("companyId");
			int userId = (int) request.getPayload().get("userId");
			int optionId = (int) request.getPayload().get("optionId");
			boolean active = (boolean) request.getPayload().get("active");
			
			restaurantManagementService.toggleActivateObject(new ItemOption(), active, optionId, companyId, userId);
			return new RestResponse("ok", null);
		} catch(Exception e) {
			return new RestResponse("error", Map.of("errorMsg", e.getMessage()));
		}
	}
	
	@PostMapping(value="/copyItemOption", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RestResponse copyItemOption(@RequestBody RestRequest request) {
		try {
			int companyId = (int) request.getPayload().get("companyId");
			int userId = (int) request.getPayload().get("userId");
			int optionId = (int) request.getPayload().get("optionId");
			ItemOption option = restaurantManagementService.copyItemOption(optionId, companyId, userId);
			return new RestResponse("ok", Map.of("option", option));
		} catch(Exception e) {
			return new RestResponse("error", Map.of("errorMsg", e.getMessage()));
		}
	}
}
