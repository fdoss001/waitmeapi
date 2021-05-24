package com.waitme.domain.user;

import java.sql.ResultSet;
import com.waitme.utils.WMLogger;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;

/**
 * Class to represent a permission
 * @author Fernando Dos Santos
 * @version 1.0 2019-04-12
 * @since 1.0 2019-04-12
 */
public class Permission extends WMDomainObject implements RowMapper<Permission> {
	private int id;
	private String name, description;
	
	@JsonIgnore
	private WMLogger log = new WMLogger(Permission.class);
	
	public Permission() {}

	/**
	 * This constructor should only be used for quick comparison checks
	 * of a permission by enum. The object should not be used afterwards
	 * @param permission the enum permission type. The ordinal should match the id
	 */
	public Permission(EPermission permission) {
		this.id = permission.ordinal() + 1;
	}
	
	public Permission(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
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

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object o) {
		try {
			if (((Permission) o).getId() == this.id) return true;
			else return false;
		} catch (ClassCastException e) {
			return false;
		}
	}
	

	@Override
	public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
		Permission permission = new Permission();
		
		try {permission.setId(rs.getInt("permission_id"));} catch(SQLException e) {throw new SQLException("No id for permission. Not found.");}
		try {permission.setName(rs.getString("permission_name"));} catch(SQLException e) {log.debug("No name for permission '" + permission.getId() + "'");}
		try {permission.setDescription(rs.getString("permission_description"));} catch(SQLException e) {log.debug("No description for permission '" + permission.getName() + "'");}

		return permission;
	}
}
