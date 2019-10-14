package com.waitme.domain.item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.waitme.domain.WMDomainObject;

/**
 * An order is a list of items
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-14
 * @since 1.0 2019-02-08
 */
public class Order extends WMDomainObject implements RowMapper<Order> {
	private int id;
	private List<ItemInOrder> items;
	
	public Order() {} //dummy constructor for json mapping
	
	public Order(int id, List<ItemInOrder> items) {
		this.id = id;
		this.items = items;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<ItemInOrder> getItems() {
		return items;
	}

	public void setItems(List<ItemInOrder> items) {
		this.items = items;
	}
	
	public void addItem(ItemInOrder item) {
		items.add(item);
	}
	
	@Override
	public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
		Order order = new Order();
		
		try {order.setId(rs.getInt("order_open_id"));} catch(SQLException e) {throw new SQLException("No id for order. Not found.");}

		return order;
	}
}
