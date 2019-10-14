package com.waitme.domain.item;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Class to represent an option on a menu item such as lettuce
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-14
 * @since 1.0 2019-02-14
 */
public class ItemOption extends Food implements RowMapper<ItemOption> {

	public ItemOption() {}
	
	public ItemOption(int id, String code, String name, boolean active, BigDecimal price,
			NutritionFacts nutritionFacts, String description) {
		super(id, code, name, active, price, nutritionFacts, description);
	}

	@Override
	public ItemOption mapRow(ResultSet rs, int rowNum) throws SQLException {
		ItemOption option = new ItemOption();

		option = (ItemOption) super.mapRow(rs, option, "item_option");
		
		return option;
	}
}
