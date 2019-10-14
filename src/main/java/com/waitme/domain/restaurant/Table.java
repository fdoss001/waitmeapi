package com.waitme.domain.restaurant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;

/**
 * Class to represent a table at a restaurant location
 * The table holds the current party sitting at that table
 * and the employee/waiter/waitress assigned to it.
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-14
 * @since 1.0 2019-02-08
 */
public class Table extends WMDomainObject implements IActivatable, RowMapper<Table> {
	private int id, locationid;
	private List<Integer> position;
	private List<Float> size;
	private ETableShape shape;
	private String name;
	private Party party;
	private int capacity;
	private boolean active;

	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(Table.class);
	
	public Table() {}
	
	public Table(int id, String name, int locationid, List<Integer> position, List<Float> size, ETableShape shape, int capacity, boolean active) {
		this.id = id;
		this.locationid = locationid;
		this.name = name;
		this.position = position;
		this.size = size;
		this.shape = shape;
		this.capacity = capacity;
		this.active = active;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
//	public int getId() {
//		return table_restaurantid;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getLocationid() {
		return locationid;
	}

	public void setLocationid(int locationid) {
		this.locationid = locationid;
	}

	public List<Integer> getPosition() {
		return position;
	}

	public void setPosition(List<Integer> position) {
		this.position = position;
	}

	public List<Float> getSize() {
		return size;
	}

	public void setSize(List<Float> size) {
		this.size = size;
	}

	public ETableShape getShape() {
		return shape;
	}

	public void setShape(ETableShape shape) {
		this.shape = shape;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	public Party getParty() {
		return party;
	}
	
	public void setParty(Party party) {
		this.party = party;
	}
	
	@Override
	public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
		Table table = new Table();
		
		try {table.setId(rs.getInt("table_restaurant_id"));} catch(SQLException e) {throw new SQLException("No id for table. Not found.");}
		try {table.setName(rs.getString("table_restaurant_name"));} catch(SQLException e) {log.debug("No name for table '" + table.getId() + "'");}
		try {table.setLocationid(rs.getInt("table_restaurant_location_id"));} catch(SQLException e) {log.debug("No locationid for table '" + table.getName() + "'");}
		try {
			List<Integer> pos = new ArrayList<Integer>(2);
			pos.add(rs.getInt("table_restaurant_posx"));
			pos.add(rs.getInt("table_restaurant_posy"));
			table.setPosition(pos);
		} catch(SQLException e) {log.debug("No position for table '" + table.getName() + "'");}		
		try {table.setShape(ETableShape.valueOf(rs.getString("table_restaurant_shape")));} catch(SQLException e) {log.debug("No shape for table '" + table.getName() + "'");}
		
		try {
			List<Float> size = null;
			if (table.getShape() == ETableShape.circle) {
				size = new ArrayList<Float>(1);
				size.add(Float.parseFloat(rs.getString("table_restaurant_size")));
			}
			else if (table.getShape() == ETableShape.rectangle) {
				size = new ArrayList<Float>(2);
				String[] sizeArr = rs.getString("table_restaurant_size").split(",");
				size.add(Float.parseFloat(sizeArr[0]));
				size.add(Float.parseFloat(sizeArr[1]));
			}
			table.setSize(size);
		} catch(SQLException e) {log.debug("No size for table '" + table.getName() + "'");}
		
		try {table.setCapacity(rs.getInt("table_restaurant_capacity"));} catch(SQLException e) {log.debug("No capacity for table '" + table.getName() + "'");}
		try {table.setActive(rs.getBoolean("table_restaurant_active"));} catch(SQLException e) {log.debug("No active for table '" + table.getName() + "'");}

		return table;
	}
}
