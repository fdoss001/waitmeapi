package com.waitme.domain.event;

import org.springframework.context.ApplicationEvent;

import com.waitme.domain.user.WMUser;

/**
 * NOT BEING USED
 * Class to represent a custom waitme login event
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-08
 * @since 1.0 2019-02-08
 */
public class WMUserLoginEvent extends ApplicationEvent {
	private static final long serialVersionUID = -4341186449258791938L;
	private String message;
	private WMUser wmUser;
	
	public WMUserLoginEvent(Object source, WMUser wmUser, String message) {
		super(source);
		this.message = message;
		this.wmUser = wmUser;
	}
	
	public String getMessage() {
		return message;
	}

	public WMUser getWmUser() {
		return wmUser;
	}
}
