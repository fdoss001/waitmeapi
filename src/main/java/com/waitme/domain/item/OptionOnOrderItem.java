package com.waitme.domain.item;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.utils.WMLogger;

/**
 * This is different than an option. It's an option on an item in an order
 * An example would be ketchup on a burger that's in an order
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-31
 * @since 1.0 2019-05-31
 */
public class OptionOnOrderItem extends Food implements RowMapper<OptionOnOrderItem> {
	private int orderOptionId;
	private EOptionQuantity quantity;
	//modified price. Ex. Cheese may be $0.50 but included on a burger it's $0.00
	private BigDecimal priceAdjustment;
	
	@JsonIgnore
	private WMLogger log = new WMLogger(OptionOnOrderItem.class);
	
	public OptionOnOrderItem() {} //dummy constructor for json mapping
	
	public OptionOnOrderItem(int orderOptionId, int id, String code, String name, boolean active, BigDecimal price,
			NutritionFacts nutritionFacts, String description, EOptionQuantity quantity, BigDecimal priceAdjustment) {
		super(id, code, name, active, price, nutritionFacts, description);
		this.orderOptionId = orderOptionId;
		this.quantity = quantity;
		this.priceAdjustment = priceAdjustment;
	}

	public int getOrderOptionId() {
		return orderOptionId;
	}

	public void setOrderOptionId(int orderOptionId) {
		this.orderOptionId = orderOptionId;
	}

	public EOptionQuantity getQuantity() {
		return quantity;
	}

	public void setQuantity(EOptionQuantity quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPriceAdjustment() {
		return priceAdjustment;
	}

	public void setPriceAdjustment(BigDecimal priceAdjustment) {
		this.priceAdjustment = priceAdjustment;
	}
	
	@Override
	public OptionOnOrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		OptionOnOrderItem option = new OptionOnOrderItem();
		
		option = (OptionOnOrderItem) super.mapRow(rs, option, "item_option");
		
		try {option.setOrderOptionId(rs.getInt("order_item_option_id"));} catch(SQLException e) {log.debug("No order option id for order option '" + option.getName() + "'");}
		try {option.setPriceAdjustment(rs.getBigDecimal("order_item_option_price_adjustment"));} catch(SQLException e) {log.debug("No price_adjustment for option '" + option.getName() + "'");}
		try {option.setQuantity(EOptionQuantity.valueOf(rs.getString("order_item_option_quantity")));} catch(SQLException e) {log.debug("No quantity for option '" + option.getName() + "'");}
		
		return option;
	}
}
