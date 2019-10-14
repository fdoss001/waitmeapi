package com.waitme.domain.restaurant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.waitme.domain.WMDomainObject;
import com.waitme.domain.user.PostalAddress;

/**
 * Class to represent a restaurant location
 * A location has a list of tables
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-14
 * @since 1.0 2019-02-14
 */
public class Location extends WMDomainObject implements IActivatable, RowMapper<Location> {
	private int id;
	private String name, phone;
	private PostalAddress address;
	private boolean active;
	private WeekTimeRange dateTimesOpen;
	private ERestaurantType type;
	private List<Table> tables;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(Location.class);
	
	public Location() {}
	
	public Location(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Location(int id, String name, ERestaurantType type, String address, String phone, WeekTimeRange dateTimesOpen, boolean active, List<Table> tables) {
		this(id, name);
		this.address = new PostalAddress(address);
		this.phone = phone;
		this.type = type;
		this.tables = tables;
		this.dateTimesOpen = dateTimesOpen;
		this.active = active;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PostalAddress getAddress() {
		return address;
	}

	@JsonSetter("address")
	public void setAddress(PostalAddress address) {
		this.address = address;
	}
	
	public void setAddress(String address) {
		this.address = new PostalAddress(address);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public ERestaurantType getType() {
		return type;
	}

	public void setType(ERestaurantType type) {
		this.type = type;
	}

	public List<Table> getTables() {
		return tables;
	}
	
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public Table getTable(int tableid) throws NoSuchElementException {
		for (Table t : tables) {
			if (t.getId() == tableid)
				return t;
		}
		throw new NoSuchElementException("A table with that id does not exist");
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public WeekTimeRange getDateTimesOpen() {
		return dateTimesOpen;
	}

	public void setDateTimesOpen(WeekTimeRange dateTimesOpen) {
		this.dateTimesOpen = dateTimesOpen;
	}
	
	@Override
	public boolean equals(Object o) {
		try {
			if (((Location) o).getId() == this.id) return true;
			else return false;
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
		Location location = new Location();
		
		try {location.setId(rs.getInt("location_id"));} catch(SQLException e) {throw new SQLException("No id for location. Not found.");}
		try {location.setName(rs.getString("location_name"));} catch(SQLException e) {log.debug("No name for location '" + location.getId() + "'");}
		try {location.setType(ERestaurantType.valueOf(rs.getString("location_type")));} catch(SQLException e) {log.debug("No type for location '" + location.getName() + "'");}
		try {location.setAddress(rs.getString("location_address"));} catch(SQLException e) {log.debug("No address for location '" + location.getName() + "'");}
		try {location.setPhone(rs.getString("location_phone"));} catch(SQLException e) {log.debug("No phone for location '" + location.getName() + "'");}
		try {location.setDateTimesOpen(new WeekTimeRange(rs.getString("location_dtms_open")));} catch(SQLException e) {log.debug("No dtms open for location '" + location.getName() + "'");}
		try {location.setActive(rs.getBoolean("location_active"));} catch(SQLException e) {log.debug("No active for location '" + location.getName() + "'");}
		
		return location;
	}
}
