package com.waitme.domain.item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * A class to represent a main category
 * Main categories have sub categories
 * @author Fernando Dos Santos
 * @version 1.0 2019-03-06
 * @since 1.0 2019-03-06
 */
public class MainCategory extends FoodListHolder implements RowMapper<MainCategory> {
	private List<SubCategory> subCategories;

	public MainCategory() {}
	
	public MainCategory(int id, String code, String name, boolean active) {
		super(id, code, name, active);
		this.subCategories = null;
	}
	
	public List<SubCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}
	
	@Override
	public MainCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
		MainCategory mainCategory = new MainCategory();
		
		mainCategory = (MainCategory) super.mapRow(rs, mainCategory, "category");
		
		return mainCategory;
	}
}
