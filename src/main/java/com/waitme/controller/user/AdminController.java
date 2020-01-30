package com.waitme.controller.user;

import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.waitme.domain.web.RestResponse;
import com.waitme.exception.NoResultException;
import com.waitme.service.RestaurantManagementService;
import com.waitme.domain.item.Menu;

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
}
