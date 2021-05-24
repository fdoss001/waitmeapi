package com.waitme.domain.restaurant;

import java.sql.ResultSet;
import com.waitme.utils.WMLogger;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;

/**
 * Class to represent a party of people sitting at a table
 * @author Gabriel Cardona
 * @version 1.0 2019-06-18
 * @since 1.0 2019-02-08
 */
public class Party extends WMDomainObject implements IActivatable, RowMapper<Party> {
	private int id;
	private List<Guest> guests;
	private int tableId;
	private int employeeId;
	private boolean active;
	
	@JsonIgnore
	private WMLogger log = new WMLogger(Party.class);
	
	public Party() {}
	
	public Party(int id, int tableId, int employeeId, boolean active) {
		this.id = id;
		this.tableId = tableId;
		this.employeeId = employeeId;
		this.active = active;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public List<Guest> getGuests() {
		return guests;
	}

	public void setGuests(List<Guest> guests) {
		this.guests = guests;
	}
	
	public int getEmployeeId() {
		return employeeId;
	}
	
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public Party mapRow(ResultSet rs, int rowNum) throws SQLException {
		Party party = new Party();
		
		try {party.setId(rs.getInt("party_id"));} catch(SQLException e) {throw new SQLException("No id for party. Not found.");}
		try {party.setTableId(rs.getInt("party_table_restaurant_id"));} catch(SQLException e) {log.debug("No tableid for party '" + party.getId() + "'");}
		try {party.setEmployeeId(rs.getInt("party_employee_id"));} catch(SQLException e) {log.debug("No employeeid for party '" + party.getId() + "'");}
		try {party.setActive(rs.getBoolean("party_active"));} catch(SQLException e) {log.debug("No active for party '" + party.getId() + "'");}
		
		return party;
	}
}
