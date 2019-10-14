package com.waitme.persistence;

import java.util.Properties;

import org.springframework.stereotype.Component;

import com.waitme.config.WMProperties;
import com.waitme.domain.restaurant.Company;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoResultException;
import com.waitme.utils.DBUtils;

/**
 * Implementation persistence class for interacting with company like tables in DB
 * @author Fernando Dos Santos
 * @version 1.0 2019-07-22
 * @since 1.0 2019-07-22
 */
@Component
public class CompanyDAO {
	private Properties spProps;
//	private Properties messageProps;
	
	public CompanyDAO() {
		spProps = new WMProperties("storedproc.properties");
//		messageProps = new WMProperties("messages.properties");
	}
	
	public int company_ins(String name, String creatorUname) throws DuplicateException {
		return DBUtils.insertHelper(-1, spProps.getProperty("company_ins"), name, creatorUname);
	}
	
	public void company_upd(int companyid, String name, String updaterUname) throws DuplicateException {
		DBUtils.updateHelper(-1, spProps.getProperty("company_upd"), companyid, name, updaterUname);
	}
	
	public Company company_sel(int companyid) throws NoResultException {
		return DBUtils.objectSelectHelper(-1, spProps.getProperty("company_sel"), new Company(), companyid);
	}
	
	public void company_settings_upd(int companyid, String iconPath, String themePath, String updaterUname) {
		DBUtils.runSPHelper(-1, spProps.getProperty("company_settings_upd"), companyid, iconPath, themePath, updaterUname);
	}
	
	public void create_new_schema(String dbname) {
		DBUtils.runSPHelper(-1, spProps.getProperty("create_new_schema"), dbname);
	}
	
	public int company_user_sel_cnt(String uname) throws NoResultException {
		return DBUtils.primitiveSelectHelper(-1, spProps.getProperty("company_user_sel_cnt"), Integer.class, uname);
	}
}
