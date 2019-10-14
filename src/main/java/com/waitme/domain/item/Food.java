package com.waitme.domain.item;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An abstract class to represent any food item
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-14
 * @since 1.0 2019-02-14
 */
public abstract class Food extends FoodListHolder {
	/*
	 * this is the final price. 
	 * It is updated accordingly based on the adjustments,
	 * promos, and options.
	 */
	private BigDecimal price;
	private NutritionFacts nutritionFacts;
	private String description;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(Food.class);
	
	public Food() {}
	
	public Food(int id, String code, String name, boolean active, BigDecimal price, NutritionFacts nutritionFacts, String description) {
		super(id, code, name, active);
		this.price = price;
		this.nutritionFacts = nutritionFacts;
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public NutritionFacts getNutritionFacts() {
		return nutritionFacts;
	}

	public void setNutritionFacts(NutritionFacts nutritionFacts) {
		this.nutritionFacts = nutritionFacts;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Food mapRow(ResultSet rs, Food food, String colPrefix) throws SQLException {

		food = (Food) super.mapRow(rs, food, colPrefix);
		
		try {food.setDescription(rs.getString(colPrefix + "_description"));} catch(SQLException e) {log.debug("No description for item '" + food.getName() + "'");}
		try {food.setPrice(rs.getBigDecimal(colPrefix + "_base_price"));} catch(SQLException e) {log.debug("No price for item '" + food.getName() + "'");}
		
		//nutrition facts
		try {
			int i = rs.getInt("nutrition_facts_id"); //to check if info is available. If it is, use mapper
			if (i == 0) {throw new SQLException();}
			NutritionFacts nf = new NutritionFacts();
			nf = nf.mapRow(rs, 0);
			food.setNutritionFacts(nf);
		} catch(SQLException e) {log.debug("No complete nutrition facts info for " + colPrefix + " '" + food.getName() + "'");}
		
		return food;
	}
}
