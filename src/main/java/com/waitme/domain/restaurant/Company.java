package com.waitme.domain.restaurant;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;

/**
 * Class to represent a company object
 * @author Fernando Dos Santos
 * @version 1.0 2019-06-25
 * @since 1.0 2019-06-25
 */
public class Company extends WMDomainObject implements RowMapper<Company> {
	private int id;
	private String name, dbname;
	private CompanySettings settings;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(Company.class);
	
	public Company() {}
	
	public Company(int id) {
		this.id = id;
	}
	
	public Company(int id, String name, String dbname, CompanySettings settings) {
		this.id = id;
		this.name = name;
		this.dbname = dbname;
		this.settings = settings;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public CompanySettings getSettings() {
		return settings;
	}

	public void setSettings(CompanySettings settings) {
		this.settings = settings;
	}
	
	@Override
	public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
		Company company = new Company();
		
		try {company.setId(rs.getInt("company_id"));} catch(SQLException e) {throw new SQLException("No id for company. Not found.");}
		try {company.setName(rs.getString("company_name"));} catch(SQLException e) {log.debug("No name for company '" + company.getId() + "'");}
		try {company.setDbname(rs.getString("company_dbname"));} catch(SQLException e) {log.debug("No dbname for company '" + company.getName() + "'");}
		
		//settings
		try {
			CompanySettings cs = new CompanySettings();
			cs = cs.mapRow(rs, 0);
			company.setSettings(cs);
		} catch(SQLException e) {throw new SQLException("No complete company settings for  company '" + company.getName() + "'");}

		return company;
	}
}
