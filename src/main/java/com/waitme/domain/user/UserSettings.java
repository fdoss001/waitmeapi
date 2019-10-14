package com.waitme.domain.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;

/**
 * Class to represent an employee's settings
 * @author Fernando Dos Santos
 * @version 1.0 2019-04-10
 * @since 1.0 2019-04-10
 */
public class UserSettings extends WMDomainObject implements RowMapper<UserSettings> {
	private String themePath;
	private int defaultPosSubModuleId;

	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(UserSettings.class);
	
	public UserSettings() {}

	public UserSettings(String themePath, int defaultPosSubModuleId) {
		this.themePath = themePath;
		this.defaultPosSubModuleId = defaultPosSubModuleId;
	}

	public String getThemePath() {
		return themePath;
	}

	public void setThemePath(String themePath) {
		this.themePath = themePath;
	}

	public int getDefaultPosSubModuleId() {
		return defaultPosSubModuleId;
	}

	public void setDefaultPosSubModuleId(int defaultPosSubModuleId) {
		this.defaultPosSubModuleId = defaultPosSubModuleId;
	}

	@Override
	public UserSettings mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserSettings settings = new UserSettings();

		try {settings.setThemePath(rs.getString("employee_settings_theme_path"));} catch(SQLException e) {log.debug("No theme path for settings");}
		try {settings.setDefaultPosSubModuleId(rs.getInt("employee_settings_default_pos_sub_module_id"));} catch(SQLException e) {log.debug("No default pos module for settings");}
		
		return settings;
	}
}
