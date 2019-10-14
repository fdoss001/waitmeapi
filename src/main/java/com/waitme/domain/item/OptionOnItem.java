package com.waitme.domain.item;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is different than an option. It's an option on an item.
 * An example would be ketchup on a burger.
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-14
 * @since 1.0 2019-02-14
 */
public class OptionOnItem extends Food implements RowMapper<OptionOnItem> {
	private EOptionQuantity quantity;
	//modified price. Ex. Cheese may be $0.50 but included on a burger it's $0.00
	private BigDecimal priceAdjustment;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(OptionOnItem.class);
	
	public OptionOnItem() {}
	
	public OptionOnItem(int id, String code, String name, boolean active, BigDecimal price,
			NutritionFacts nutritionFacts, String description, EOptionQuantity quantity, BigDecimal priceAdjustment) {
		super(id, code, name, active, price, nutritionFacts, description);
		this.quantity = quantity;
		this.priceAdjustment = priceAdjustment;
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
	public OptionOnItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		OptionOnItem option = new OptionOnItem();
		
		option = (OptionOnItem) super.mapRow(rs, option, "item_option");
		
		try {option.setPriceAdjustment(rs.getBigDecimal("item_item_option_price_adjustment"));} catch(SQLException e) {log.debug("No price_adjustment for option '" + option.getName() + "'");}
		try {option.setQuantity(EOptionQuantity.valueOf(rs.getString("item_item_option_quantity")));} catch(SQLException e) {log.debug("No quantity for option '" + option.getName() + "'");}
		
		return option;
	}
}
