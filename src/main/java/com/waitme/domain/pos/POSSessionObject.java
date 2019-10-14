package com.waitme.domain.pos;

import java.util.ArrayList;
import java.util.List;

import com.waitme.domain.user.WMUser;

/**
 * Custom class to represent that a pos master session is active.
 * This object will be assigned to all active POS machines.
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-08
 * @since 1.0 2019-02-08
 */
public class POSSessionObject {

	private List<WMUser> loggedInUsers;
	
	public POSSessionObject() {
		this.loggedInUsers = new ArrayList<WMUser>();
	}

	public List<WMUser> getLoggedInUsers() {
		return loggedInUsers;
	}

	public void setLoggedInUsers(List<WMUser> loggedInUsers) {
		this.loggedInUsers = loggedInUsers;
	}
}
