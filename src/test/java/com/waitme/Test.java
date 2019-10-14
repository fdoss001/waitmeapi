package com.waitme;

import com.waitme.domain.restaurant.Location;
import com.waitme.domain.user.WMUser;
import com.waitme.exception.NoResultException;
import com.waitme.persistence.AdminDAO;
import com.waitme.persistence.UserDAO;
import com.waitme.service.AdminService;
import com.waitme.service.DBConnectionService;
import com.waitme.service.UserService;

@SuppressWarnings("unused")
public class Test {
	public static void main(String[] args) throws NoResultException {
		new DBConnectionService();
		UserService userService = new UserService(new UserDAO(), new AdminDAO());
//		AdminService adminService = new AdminService(new AdminDAO());
		
		WMUser user = userService.getWMUser("fdoss");
//		Location location = adminService.getSimpleLocation(1000000, 1);
		System.out.println(user);
	}
}
