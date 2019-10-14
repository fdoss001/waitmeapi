package com.waitme.domain.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;

/**
 * Class to represent an ajax call in a sub module
 * @author Fernando Dos Santos
 * @version 1.0 2019-04-01
 * @since 1.0 2019-04-01
 */
public class AjaxModule extends WMDomainObject implements RowMapper<AjaxModule> {
	private int id;
	private String name;
	private String url;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(AjaxModule.class);
	
	public AjaxModule() {} 
	
	public AjaxModule(int id, String name, String url) {
		this.id = id;
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public AjaxModule mapRow(ResultSet rs, int rowNum) throws SQLException {
		AjaxModule ajaxModule = new AjaxModule();
		
		try {ajaxModule.setId(rs.getInt("ajax_module_id"));} catch(SQLException e) {throw new SQLException("No id for ajax module. Not found.");}
		try {ajaxModule.setName(rs.getString("ajax_module_name"));} catch(SQLException e) {log.debug("No name for ajax module '" + ajaxModule.getId() + "'");}
		try {ajaxModule.setUrl(rs.getString("ajax_module_url"));} catch(SQLException e) {log.debug("No url for ajax module '" + ajaxModule.getName() + "'");}
		
		return ajaxModule;
	}
}
