package com.waitme.domain.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;

/**
 * Class to represent a sub module
 * @author Fernando Dos Santos
 * @version 1.0 2019-03-26 
 * @since 1.0 2019-03-26
 */
public class SubModule extends WMDomainObject implements RowMapper<SubModule> {
	private int id;
	private String name;
	private String url;
	private boolean sel, ins, upd, del;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(SubModule.class);
	
	public SubModule() {}
	
	public SubModule(int id, String name, String url) {
		this(id, name, url, true, true, true, false);
	}
	
	public SubModule(int id, String name, String url, boolean sel, boolean ins, boolean upd, boolean del) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.sel = sel;
		this.ins = ins;
		this.upd = upd;
		this.del = del;
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

	public boolean isSel() {
		return sel;
	}

	public void setSel(boolean sel) {
		this.sel = sel;
	}

	public boolean isIns() {
		return ins;
	}

	public void setIns(boolean ins) {
		this.ins = ins;
	}

	public boolean isUpd() {
		return upd;
	}

	public void setUpd(boolean upd) {
		this.upd = upd;
	}

	public boolean isDel() {
		return del;
	}

	public void setDel(boolean del) {
		this.del = del;
	}
	
	@Override
	public SubModule mapRow(ResultSet rs, int rowNum) throws SQLException {
		SubModule subModule = new SubModule();
		
		try {subModule.setId(rs.getInt("sub_module_id"));} catch(SQLException e) {throw new SQLException("No id for sub module. Not found.");}
		try {subModule.setName(rs.getString("sub_module_name"));} catch(SQLException e) {log.debug("No name for sub module '" + subModule.getId() + "'");}
		try {subModule.setUrl(rs.getString("sub_module_url"));} catch(SQLException e) {log.debug("No url for sub module '" + subModule.getName() + "'");}
		
		try {subModule.setSel(rs.getBoolean("employee_sub_module_sel"));} catch(SQLException e) {log.debug("No sel for sub module '" + subModule.getName() + "'");}
		try {subModule.setIns(rs.getBoolean("employee_sub_module_ins"));} catch(SQLException e) {log.debug("No ins for sub module '" + subModule.getName() + "'");}
		try {subModule.setUpd(rs.getBoolean("employee_sub_module_upd"));} catch(SQLException e) {log.debug("No upd for sub module '" + subModule.getName() + "'");}
		try {subModule.setDel(rs.getBoolean("employee_sub_module_del"));} catch(SQLException e) {log.debug("No del for sub module '" + subModule.getName() + "'");}

		return subModule;
	}
}
