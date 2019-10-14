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
 * This is different than an item. It's an item in a meal.
 * An example would be a side of fries.
 * An item can have a list of possible valid substitutes
 * @author Fernando Dos Santos
 * @version 1.0 2019-03-06
 * @since 1.0 2019-03-06
 */
public class ItemInMeal extends Food implements RowMapper<ItemInMeal> {
	//modified price. Ex. Fries may be $6.00 but included in a burger meal it's $0.00
	private BigDecimal priceAdjustment;
	private List<ItemInMeal> substituteChoices;
	private List<OptionOnItem> options;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(ItemInMeal.class);
	
	public ItemInMeal() {}
	
	public ItemInMeal(int id, String code, String name, boolean active, BigDecimal price, NutritionFacts nutritionFacts, String description, List<OptionOnItem> options, BigDecimal priceAdjustment, List<ItemInMeal> substituteChoices) {
		super(id, code, name, active, price, nutritionFacts, description);
		this.options = options;
		this.priceAdjustment = priceAdjustment;
		this.substituteChoices = substituteChoices;
	}

	public List<OptionOnItem> getOptions() {
		return options;
	}

	public void setOptions(List<OptionOnItem> options) {
		this.options = options;
	}
	
	public BigDecimal getPriceAdjustment() {
		return priceAdjustment;
	}

	public void setPriceAdjustment(BigDecimal priceAdjustment) {
		this.priceAdjustment = priceAdjustment;
	}

	public List<ItemInMeal> getSubstituteChoices() {
		return substituteChoices;
	}

	public void setSubstituteChoices(List<ItemInMeal> substituteChoices) {
		this.substituteChoices = substituteChoices;
	}
	
	@Override
	public ItemInMeal mapRow(ResultSet rs, int rowNum) throws SQLException {
		ItemInMeal item = new ItemInMeal();
		
		item = (ItemInMeal) super.mapRow(rs, item, "item");
		
		try {item.setPriceAdjustment(rs.getBigDecimal("meal_item_price_adjustment"));} catch(SQLException e) {log.debug("No price adjustment for meal item '" + item.getName() + "'");}
		
		
		return item;
	}
}
