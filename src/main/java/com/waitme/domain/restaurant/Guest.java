package com.waitme.domain.restaurant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;
import com.waitme.domain.item.Order;
import com.waitme.utils.WMLogger;

/**
 * Class to represent a party guest
 * @author Gabriel Cardona
 * @version 1.0 2019-06-18
 * @since 1.0 2019-06-09
 */
public class Guest extends WMDomainObject implements RowMapper<Guest> {
	private int id;
	private int waitmeUserid;
	private int partyid;
	private String fname;
	private String lname;
	private boolean active;
	private List<Order> orders;
	
	@JsonIgnore
	private WMLogger log = new WMLogger(Guest.class);
	
	public Guest() {}
	
	public Guest(int id, int waitmeUserid, int partyid, String fname, String lname, boolean active) {
		this.id = id;
		this.waitmeUserid = waitmeUserid;
		this.partyid = partyid;
		this.fname  = fname;
		this.lname = lname;
		this.active = active;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWaitmeUserid() {
		return waitmeUserid;
	}

	public void setWaitmeUserid(int waitmeUserid) {
		this.waitmeUserid = waitmeUserid;
	}

	public int getPartyid() {
		return partyid;
	}

	public void setPartyid(int partyid) {
		this.partyid = partyid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
	public Guest mapRow(ResultSet rs, int rowNum) throws SQLException {
		Guest guest = new Guest();
		
		try {guest.setId(rs.getInt("guest_id"));} catch(SQLException e) {throw new SQLException("No id for guest. Not found.");}
		try {guest.setFname(rs.getString("guest_fname"));} catch(SQLException e) {log.debug("No fname for guest '" + guest.getId() + "'");}
		try {guest.setLname(rs.getString("guest_lname"));} catch(SQLException e) {log.debug("No lname for guest '" + guest.getFname() + "'");}
		try {guest.setWaitmeUserid(rs.getInt("guest_waitme_user_id"));} catch(SQLException e) {log.debug("No waitme userid for guest '" + guest.getFname() + "'");}
		try {guest.setPartyid(rs.getInt("guest_party_id"));} catch(SQLException e) {log.debug("No partyid for guest '" + guest.getFname() + "'");}
		try {guest.setActive(rs.getBoolean("guest_active"));} catch(SQLException e) {log.debug("No active for guest '" + guest.getFname() + "'");}
		
		return guest;
	}
}
