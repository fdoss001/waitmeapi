package com.waitme.domain.item;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;
import com.waitme.domain.restaurant.IActivatable;
import com.waitme.utils.WMLogger;

/**
 * An abstract class to represent any item that has a code, name, id, and active
 * @author Fernando Dos Santos
 * @version 1.0 2019-03-06
 * @since 1.0 2019-03-06
 */
public abstract class FoodListHolder extends WMDomainObject implements IActivatable {
	private int id;
	private String code;
	private String name;
	private boolean active;
	
	@JsonIgnore
	private WMLogger log = new WMLogger(FoodListHolder.class);
	
	public FoodListHolder() {}
	
	public FoodListHolder(int id, String code, String name, boolean active) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.active = active;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public FoodListHolder mapRow(ResultSet rs, FoodListHolder flh, String colPrefix) throws SQLException {

		try {flh.setId(rs.getInt(colPrefix + "_id"));} catch(SQLException e) {throw new SQLException("No id for food list holder. Not found.");}
		try {flh.setCode(rs.getString(colPrefix + "_code"));} catch(SQLException e) {log.debug("No code for " + colPrefix + " '" + flh.getId() + "'");}
		try {flh.setName(rs.getString(colPrefix + "_name"));} catch(SQLException e) {log.debug("No name for " + colPrefix + " '" + flh.getId() + "'");}		
		try {flh.setActive(rs.getBoolean(colPrefix + "_active"));} catch(SQLException e) {log.debug("No active for " + colPrefix + " '" + flh.getName() + "'");}
		
		return flh;
	}
}
