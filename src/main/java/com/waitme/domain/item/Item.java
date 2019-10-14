package com.waitme.domain.item;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * Class to represent an item on the menu
 * An item can have options on it
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-14
 * @since 1.0 2019-02-14
 */
public class Item extends Food implements RowMapper<Item> {

	private List<OptionOnItem> options;
	
	public Item() {}
	
	public Item(int id, String code, String name, boolean active, BigDecimal price,
			NutritionFacts nutritionFacts, String description, List<OptionOnItem> options) {
		super(id, code, name, active, price, nutritionFacts, description);
		this.options = options;
	}

	public List<OptionOnItem> getOptions() {
		return options;
	}

	public void setOptions(List<OptionOnItem> options) {
		this.options = options;
	}

	@Override
	public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
		Item item = new Item();
		
		item = (Item) super.mapRow(rs, item, "item");
		
		return item;
	}
}
