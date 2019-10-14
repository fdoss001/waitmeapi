package com.waitme.domain.user;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;
import com.waitme.domain.restaurant.IActivatable;
import com.waitme.domain.restaurant.Location;

/**
 * Class to represent a user of the application, usually employees
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-20
 * @since 1.0 2019-02-20
 */
public class Position extends WMDomainObject implements IActivatable, RowMapper<Position> {
	private int id;
	private String code, name;
	private EPayType payType;
	private BigDecimal basePay;
	private Location location; //
	private boolean active;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(Position.class);
	
	public Position() {}
	
	public Position(int id, String code, String name, EPayType payType, BigDecimal basePay, Location location, boolean active) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.payType = payType;
		this.basePay = basePay;
		this.location = location;
		this.active = active;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EPayType getPayType() {
		return payType;
	}

	public void setPayType(EPayType payType) {
		this.payType = payType;
	}

	public BigDecimal getBasePay() {
		return basePay;
	}

	public void setBasePay(BigDecimal basePay) {
		this.basePay = basePay;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public Position mapRow(ResultSet rs, int rowNum) throws SQLException {
		Position position = new Position();
		
		try {position.setId(rs.getInt("position_role_id"));} catch(SQLException e) {throw new SQLException("No id for position. Not found.");}
		try {position.setCode(rs.getString("position_role_code"));} catch(SQLException e) {log.debug("No code for position '" + position.getId() + "'");}
		try {position.setName(rs.getString("position_role_name"));} catch(SQLException e) {log.debug("No name for position '" + position.getId() + "'");}
		try {position.setPayType(EPayType.valueOf(rs.getString("position_role_type")));} catch(SQLException e) {log.debug("No type for position '" + position.getName() + "'");}
		try {position.setBasePay(rs.getBigDecimal("position_role_base_pay"));} catch(SQLException e) {log.debug("No pay for position '" + position.getName() + "'");}		
		try {position.setActive(rs.getBoolean("position_role_active"));} catch(SQLException e) {log.debug("No active for position '" + position.getName() + "'");}
		
		//location
		try {
			int i = rs.getInt("location_id"); //to check if info is available. If it is, use mapper
			if (i == 0) {throw new SQLException();}
			Location loc = new Location();
			loc = loc.mapRow(rs, rowNum);
			position.setLocation(loc);
		} catch(SQLException e) {log.debug("No location information for position '" + position.getName() + "'");}
		
		return position;
	}
}