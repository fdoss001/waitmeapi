package com.waitme.domain.item;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is different than an item. It's an item in an order.
 * @author Fernando Dos Santos
 * @version 1.0 2019-03-06
 * @since 1.0 2019-03-06
 */
public class ItemInOrder extends Food implements RowMapper<ItemInOrder> {
	private int orderItemId;
	//modified price. Ex. Fries may be $6.00 but included in a burger meal it's $0.00
	private BigDecimal priceAdjustment;
	private boolean voided;
	private List<OptionOnOrderItem> options;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(ItemInOrder.class);
	
	public ItemInOrder() {} //dummy constructor for json mapping
	
	public ItemInOrder(int orderItemId, int id, String code, String name, boolean active, BigDecimal price, NutritionFacts nutritionFacts, String description, List<OptionOnOrderItem> options, BigDecimal priceAdjustment, boolean voided) {
		super(id, code, name, active, price, nutritionFacts, description);
		this.orderItemId = orderItemId;
		this.priceAdjustment = priceAdjustment;
		this.voided = voided;
		this.options = options;
	}

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public BigDecimal getPriceAdjustment() {
		return priceAdjustment;
	}

	public void setPriceAdjustment(BigDecimal priceAdjustment) {
		this.priceAdjustment = priceAdjustment;
	}

	public boolean isVoided() {
		return voided;
	}

	public void setVoided(boolean voided) {
		this.voided = voided;
	}
	
	public List<OptionOnOrderItem> getOptions() {
		return options;
	}

	public void setOptions(List<OptionOnOrderItem> options) {
		this.options = options;
	}
	
	public void addOption(OptionOnOrderItem option) {
		options.add(option);
	}

	@Override
	public ItemInOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
		ItemInOrder item = new ItemInOrder();
		
		item = (ItemInOrder) super.mapRow(rs, item, "item");
		
		try {item.setOrderItemId(rs.getInt("order_item_id"));} catch(SQLException e) {log.debug("No order item id for order item '" + item.getName() + "'");}
		try {item.setPriceAdjustment(rs.getBigDecimal("order_item_price_adjustment"));} catch(SQLException e) {log.debug("No price adjustment for order item '" + item.getName() + "'");}
		try {item.setVoided(rs.getBoolean("order_item_voided"));} catch(SQLException e) {log.debug("No voided for order item '" + item.getName() + "'");}
		
		return item;
	}
}
