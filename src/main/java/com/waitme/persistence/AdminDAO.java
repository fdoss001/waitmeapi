package com.waitme.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.waitme.config.WMProperties;
import com.waitme.domain.restaurant.Location;
import com.waitme.domain.user.Position;
import com.waitme.domain.user.SubModule;
import com.waitme.utils.DBUtils;
import com.waitme.exception.DeleteException;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoResultException;
import com.waitme.domain.user.Module;
import com.waitme.domain.user.Permission;

/**
 * Implementation persistence class for interacting with administrative tables
 * @author Fernando Dos Santos
 * @version 1.0 2019-04-01
 * @since 1.0 2019-04-01
 */
@Component
public class AdminDAO {
	private Properties spProps;
	
	public AdminDAO() {
		spProps = new WMProperties("storedproc.properties");		
	}
	
	/* *** LOCATIONS *** */
	public Location location_sel(int companyid, int locationid) throws NoResultException {		
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("location_sel"), new Location(), locationid);
	}
	
	public List<Location> location_sel_all(int companyid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("location_sel_all"), new Location());
	}
	
	public Location location_sel_minimal(int companyid, int locationid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("location_sel_minimal"), new Location(), locationid);
	}
	
	public List<Location> location_sel_all_minimal(int companyid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("location_sel_all_minimal"), new Location());
	}
	
	public int location_ins(int companyid, String name, String type, String address, String phone, String dateTimesOpen, int creatorid) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("location_ins"), name, type, address, phone, dateTimesOpen, creatorid);
	}
	
	public void location_del(int companyid, int locationid) throws DeleteException {
		DBUtils.deleteHelper(companyid, spProps.getProperty("location_del"), locationid);
	}
	
	public void location_upd(int companyid, int locationid, String name, String type, String address, String phone, String dateTimesOpen, int lastupdateUserid) throws DuplicateException {
		DBUtils.updateHelper(companyid, spProps.getProperty("location_upd"), locationid, name, type, address, phone, dateTimesOpen, lastupdateUserid);
	}
	
	
	/* *** POSITIONS *** */
	public Position position_role_sel(int companyid, int positionid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("position_role_sel"), new Position(), positionid);
	}
	
	public Position position_role_sel_employee(int companyid, int employeeid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("position_role_sel_employee"), new Position(), employeeid);
	}
	
	public List<Position> position_role_sel_all(int companyid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("position_role_sel_all"), new Position());
	}
	
	public int position_role_ins(int companyid, String code, String name, String payType, BigDecimal basePay, int locationdid, int lastupdateUserid) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("position_role_ins"), code, name, payType, basePay, locationdid, lastupdateUserid);
	}
	
	public void position_del(int companyid, int positionid) throws DeleteException {
		DBUtils.deleteHelper(companyid, spProps.getProperty("position_del"), positionid);
	}
	
	public void position_role_upd(int companyid, int positionid, String code, String name, String payType, BigDecimal basePay, int locationdid, int lastupdateUserid) throws DuplicateException {
		DBUtils.updateHelper(companyid, spProps.getProperty("position_role_upd"), positionid, code, name, payType, basePay, locationdid, lastupdateUserid);
	}
	
	
	/* *** MODULES *** */
	public List<Module> module_sel_all() throws NoResultException {
		return DBUtils.listSelectHelper(-1, spProps.getProperty("module_sel_all"), new Module());
	}
	
	public List<SubModule> sub_module_module_sel(int moduleid) throws NoResultException {
		return DBUtils.listSelectHelper(-1, spProps.getProperty("sub_module_module_sel"), new SubModule(), moduleid);
	}
	
	public List<Permission> permission_sel_all() throws NoResultException {
		return DBUtils.listSelectHelper(-1, spProps.getProperty("permission_sel_all"), new Permission());
	}	
}
