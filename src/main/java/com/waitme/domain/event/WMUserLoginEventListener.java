package com.waitme.domain.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.waitme.domain.user.WMUser;
import com.waitme.service.UserService;

/**
 * NOT BEING USED
 * Class to represent a custom waitme login event listener
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-08
 * @since 1.0 2019-02-08
 */
@Component
public class WMUserLoginEventListener {
	@Autowired
	private UserService userService;
	Logger log = LoggerFactory.getLogger(WMUserLoginEventListener.class);
	
	@EventListener
	public void onWMUserLoginEvent(WMUserLoginEvent event) {
		WMUser wmUser = event.getWmUser();
		if (userService.getLoggedInUsers().containsKey(wmUser.getId())) {
			log.warn(wmUser.getUname() + " is already logged in elsewhere");
			log.warn("Cookie: " + userService.getLoggedInUsers().get(wmUser.getId()));
			//found this user already logged in elsewhere so logout
			userService.logoutWMUser(wmUser);
		}
	}

}
