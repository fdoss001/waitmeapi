package com.waitme.domain.item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.restaurant.IActivatable;
import com.waitme.domain.restaurant.WeekTimeRange;
import com.waitme.utils.WMLogger;

/**
 * Class to represent a menu with categories
 * @author Fernando Dos Santos
 * @version 1.0 2019-03-01
 * @since 1.0 2019-03-01
 */
public class Menu extends FoodListHolder implements IActivatable, RowMapper<Menu> {
	private int locationid;
	private List<MainCategory> categories;
	private WeekTimeRange dateTimesAvailable;
	
	@JsonIgnore
	private WMLogger log = new WMLogger(Menu.class);
	
	public Menu() {}
	
	public Menu(int id, int locationid, String code, String name, List<MainCategory> categories, WeekTimeRange dateTimesAvailable, boolean active) {
		super(id, code, name, active);
		this.locationid = locationid;
		this.categories = categories;
		this.dateTimesAvailable = dateTimesAvailable;
	}

	public int getLocationid() {
		return locationid;
	}

	public void setLocationid(int locationid) {
		this.locationid = locationid;
	}

	public List<MainCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<MainCategory> categories) {
		this.categories = categories;
	}

	public WeekTimeRange getDateTimesAvailable() {
		return dateTimesAvailable;
	}

	public void setDateTimesAvailable(WeekTimeRange dateTimesAvailable) {
		this.dateTimesAvailable = dateTimesAvailable;
	}
	
	@Override
	public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
		Menu menu = new Menu();
		
		menu = (Menu) super.mapRow(rs, menu, "menu");
		
		try {menu.setDateTimesAvailable(new WeekTimeRange(rs.getString("menu_dtms_available")));} catch(SQLException e) {log.debug("No dtms available for menu '" + menu.getName() + "'");}
		try {menu.setLocationid(rs.getInt("menu_location_id"));} catch(SQLException e) {log.debug("No location_id for menu '" + menu.getName() + "'");}		
		
		return menu;
	}
}
