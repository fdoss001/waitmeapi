package com.waitme.persistence;

import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.waitme.config.WMProperties;
import com.waitme.domain.restaurant.Guest;
import com.waitme.domain.restaurant.Party;
import com.waitme.domain.restaurant.Table;
import com.waitme.domain.user.WMUser;
import com.waitme.utils.DBUtils;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoResultException;

/**
 * Implementation persistence class for interacting with tables
 * related to hosting services in DB
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-20
 * @since 1.0 2019-02-20
 */
@Component
public class HostingDAO {
	private Properties spProps;
	
	public HostingDAO() {
		spProps = new WMProperties("storedproc.properties");		
	}
	
	
	/* *** EMPLOYEES *** */
	public List<WMUser> employee_clockedin_sel(int companyid, int locationid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("employee_clockedin_sel"), new WMUser(), locationid);
	}

//	public void table_restaurant_employee_upd(int companyid, int employeeid, int tableid, int updaterid) {
//		DBUtils.updateHelper(companyid, spProps.getProperty("table_restaurant_employee_upd"), employeeid, tableid, updaterid);
//	}
	
	
	/* *** TABLES *** */
	public List<Table> table_restaurant_active_sel_location(int companyid, int locationid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("table_restaurant_active_sel_location"), new Table(), locationid);
	}
	
	
	/* *** PARTIES *** */
	public Party party_active_sel_table_restaurant(int companyid, int tableid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("party_active_sel_table_restaurant"), new Party(), tableid);
	}
	
	public List<Party> party_sel_table_restaurant(int companyid, int tableid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("party_sel_table_restaurant"), new Party(), tableid);
	}
	
	public List<Party> party_active_sel_location(int companyid, int locationid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("party_active_sel_location"), new Party(), locationid);
	}
	
	public int party_ins(int companyid, int tableid, int employeeid, int creatorid) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("party_ins"), tableid, employeeid, creatorid);
	}
	
	public void party_upd(int companyid, int partyid, int tableid, int employeeid, boolean active, int updaterid) throws DuplicateException {
		DBUtils.updateHelper(companyid, spProps.getProperty("party_upd"), partyid, tableid, employeeid, active, updaterid);
	}
	
	
	/* *** GUESTS *** */
	public int guest_ins(int companyid, int waitmeUserId, int partyid, String firstName, String lastName) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("guest_ins"), waitmeUserId, partyid, firstName, lastName);
	}
	
	
	public List<Guest> guest_sel_party(int companyid, int partyid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("guest_sel_party"), new Guest(), partyid);
	}
}
