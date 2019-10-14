package com.waitme.domain.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;

/**
 * Class to represent a module with submodules
 * @author Fernando Dos Santos
 * @version 1.0 2019-03-26
 * @since 1.0 2019-03-26
 */
public class Module extends WMDomainObject implements RowMapper<Module> {
	private int id;
	private String name;
	private List<SubModule> subModules;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(Module.class);
	
	public Module() {}
	
	public Module(int id, String name, List<SubModule> subModules) {
		this.id = id;
		this.name = name;
		this.subModules = subModules;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SubModule> getSubModules() {
		return subModules;
	}

	public void setSubModules(List<SubModule> subModules) {
		this.subModules = subModules;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public Module mapRow(ResultSet rs, int rowNum) throws SQLException {
		Module module = new Module();
		
		try {module.setId(rs.getInt("module_id"));} catch(SQLException e) {throw new SQLException("No id for module. Not found.");}
		try {module.setName(rs.getString("module_name"));} catch(SQLException e) {log.debug("No name for module '" + module.getId() + "'");}
		
		return module;
	}
}
