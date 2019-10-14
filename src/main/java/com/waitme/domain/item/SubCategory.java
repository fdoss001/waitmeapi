package com.waitme.domain.item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * A class to represent a sub category
 * @author Fernando Dos Santos
 * @version 1.0 2019-03-06
 * @since 1.0 2019-03-06
 */
public class SubCategory extends FoodListHolder implements RowMapper<SubCategory> {
	List<Meal> meals;
	
	public SubCategory() {}
	
	public SubCategory(int id, String code, String name, boolean active) {
		super(id, code, name, active);
		this.meals = null;
	}

	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
	}
	
	@Override
	public SubCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
		SubCategory subCategory = new SubCategory();
		
		subCategory = (SubCategory) super.mapRow(rs, subCategory, "sub_category");
		
		return subCategory;
	}
}
