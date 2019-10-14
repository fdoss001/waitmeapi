package com.waitme.domain.restaurant;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;

/**
 * Class to represent a company's settings
 * @author Fernando Dos Santos
 * @version 1.0 2019-06-25
 * @since 1.0 2019-06-25
 */
public class CompanySettings extends WMDomainObject implements RowMapper<CompanySettings> {
	private String logoPath, themePath;

	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(Company.class);
	
	public CompanySettings() {}
	
	public CompanySettings(String logoPath, String themePath) {
		this.logoPath = logoPath;
		this.themePath = themePath;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String iconPath) {
		this.logoPath = iconPath;
	}

	public String getThemePath() {
		return themePath;
	}

	public void setThemePath(String themePath) {
		this.themePath = themePath;
	}
	
	
	@Override
	public CompanySettings mapRow(ResultSet rs, int rowNum) throws SQLException {
		CompanySettings companySettings = new CompanySettings();
		
		try {companySettings.setLogoPath(rs.getString("company_settings_logo_path"));} catch(SQLException e) {log.debug("No logo path for company.");}
		try {companySettings.setThemePath(rs.getString("company_settings_theme_path"));} catch(SQLException e) {log.debug("No theme path for company.");}

		return companySettings;
	}
}
