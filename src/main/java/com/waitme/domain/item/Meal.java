package com.waitme.domain.item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * Class to represent a meal with items
 * Meals are not part of the logic that is used in orders, only in menus.
 * When a meal is added to an order, all individual items of the meal should be added instead
 * @author Fernando Dos Santos
 * @version 1.0 2019-03-06
 * @since 1.0 2019-03-06
 */
public class Meal extends FoodListHolder implements RowMapper<Meal> {

	private List<ItemInMeal> mealItems;
	
	public Meal() {}
	
	public Meal(int id, String code, String name, boolean active) {
		super(id, code, name, active);
		this.mealItems = null;
	}

	public List<ItemInMeal> getMealItems() {
		return mealItems;
	}

	public void setMealItems(List<ItemInMeal> mealItems) {
		this.mealItems = mealItems;
	}

	@Override
	public Meal mapRow(ResultSet rs, int rowNum) throws SQLException {
		Meal meal = new Meal();
		
		meal = (Meal) super.mapRow(rs, meal, "meal");
		
		return meal;
	}
}
