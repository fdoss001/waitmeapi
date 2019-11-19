package com.waitme.persistence;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Component;

import com.waitme.config.WMProperties;
import com.waitme.domain.restaurant.Location;
import com.waitme.domain.user.Module;
import com.waitme.domain.user.Permission;
import com.waitme.domain.user.SubModule;
import com.waitme.domain.user.TimeSheet;
import com.waitme.domain.user.UserSettings;
import com.waitme.domain.user.WMUser;
import com.waitme.utils.DBUtils;
import com.waitme.exception.ClockException;
import com.waitme.exception.DeleteException;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoResultException;

/**
 * Implementation persistence class for interacting with employee like tables in DB
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-19
 * @since 1.0 2019-02-08
 */
@Component
public class UserDAO {	
	private Properties spProps;
	
	public UserDAO() {
		spProps = new WMProperties("storedproc.properties");
	}
	
	public int employee_ins(int companyid, String uname, String fname, String lname, String email, String phone, String address, byte[] passwd, byte[] salt, int pin, int positionid, BigDecimal payAdjustment, int lastupdateUserid) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("employee_ins"), companyid, uname, fname, lname, email, phone, address, passwd, salt, pin, positionid, payAdjustment, lastupdateUserid);
	}
	
	public void employee_del(int companyid, int id) throws DeleteException {
		DBUtils.deleteHelper(companyid, spProps.getProperty("employee_del"), id);
	}
	
	public void employee_upd_pwd(int companyid, int id, byte[] newPasswd, byte[] salt) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("employee_upd_pwd"), id, newPasswd, salt);
	}
	
	public void employee_upd_pin(int companyid, int id, int newPin) throws DuplicateException {
		DBUtils.updateHelper(companyid, spProps.getProperty("employee_upd_pin"), id, newPin);
	}
	
	public void employee_upd_basic(int companyid, int id, String uname, String fname, String lname, String email, String phone, String address, int lastUpdateUserId) throws DuplicateException {		
		DBUtils.updateHelper(companyid, spProps.getProperty("employee_upd_basic"), id, uname, fname, lname, email, phone, address, lastUpdateUserId);
	}
	
	public void employee_upd_position(int companyid, int id, int positionid, BigDecimal payAdjustment, int lastUpdateUserId) {		
		DBUtils.runSPHelper(companyid, spProps.getProperty("employee_upd_position"), id, positionid, payAdjustment, lastUpdateUserId);
	}
	
	public WMUser employee_sel(String un) throws NoResultException {
		int companyid = DBUtils.primitiveSelectHelper(-1, spProps.getProperty("company_user_sel"), Integer.class, un);
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("employee_sel"), new WMUser(), un);
	}
	
	public WMUser employee_sel_pin(int companyid, int pin) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("employee_sel_pin"), new WMUser(), pin);
	}
	
	public int employee_sel_pin_cnt(int companyid, int pin) throws NoResultException {
		return DBUtils.primitiveSelectHelper(companyid, spProps.getProperty("employee_sel_pin_cnt"), Integer.class, pin);
	}
	
	public WMUser employee_sel_id(int companyid, int id) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("employee_sel_id"), new WMUser(), id);
	}
	
	public void employee_clockin(int companyid, int employeeid, int locationid, Date weekstart, Date timein) throws ClockException {
		try {
			DBUtils.getTemplateForCompany(companyid).update(spProps.getProperty("employee_clockin"), employeeid, locationid, weekstart, timein);
		} catch (UncategorizedSQLException e) {
			throw new ClockException(e.getMessage(), Integer.parseInt(e.getSQLException().getSQLState()));
		}
	}
	
	public void employee_clockout(int companyid, int id, Date timeout) throws ClockException {
		try {
			DBUtils.getTemplateForCompany(companyid).update(spProps.getProperty("employee_clockout"), id, timeout);
		} catch (UncategorizedSQLException e) {
			throw new ClockException(e.getMessage(), Integer.parseInt(e.getSQLException().getSQLState()));
		}
	}
	
	public void employee_break_start(int companyid, int id) throws ClockException {
		try {
			DBUtils.getTemplateForCompany(companyid).update(spProps.getProperty("employee_break_start"), id);
		} catch (UncategorizedSQLException e) {
			throw new ClockException(e.getMessage(), Integer.parseInt(e.getSQLException().getSQLState()));
		}
	}
	
	public void employee_break_end(int companyid, int id) throws ClockException {
		try {
			DBUtils.getTemplateForCompany(companyid).update(spProps.getProperty("employee_break_end"), id);
		} catch (UncategorizedSQLException e) {
			throw new ClockException(e.getMessage(), Integer.parseInt(e.getSQLException().getSQLState()));
		}
	}
	
	public List<TimeSheet> timesheet_day_sel_all_week(int companyid, int employeeid, Date startOfWeek) throws NoResultException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("timesheet_day_sel_all_week"), new TimeSheet(), employeeid, format.format(startOfWeek));
	}
	
	public TimeSheet timesheet_day_sel_current_open(int companyid, int employeeid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("timesheet_day_sel_current_open"), new TimeSheet(), employeeid);
	}
	
	public void employee_loginout(int companyid, int id, boolean login) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("employee_loginout"), id, login);
	}
	
	public void employee_loginout_pos(int companyid, int id, boolean login) throws ClockException {
		try {
			DBUtils.runSPHelper(companyid, spProps.getProperty("employee_loginout_pos"), id, login);
		} catch (UncategorizedSQLException e) {
			throw new ClockException(e.getMessage(), Integer.parseInt(e.getSQLException().getSQLState()));
		}
	}
	
	public Map<String, byte[]> employee_sel_pwd(int companyid, int id) throws NoResultException {
		Map<String, Object> passAndSalt = DBUtils.getTemplateForCompany(companyid).queryForMap(spProps.getProperty("employee_sel_pwd"), id);
		if (passAndSalt == null) {
			throw new NoResultException("The user '" + id + "' does not exist for company '" + companyid + "'");
		}
		byte[] pass = (byte[]) passAndSalt.get("employee_passwd");
		byte[] salt = (byte[]) passAndSalt.get("employee_salt");
		Map<String, byte[]> passAndSaltBytes = new HashMap<String, byte[]>(2);
		passAndSaltBytes.put("employee_passwd", pass);
		passAndSaltBytes.put("employee_salt", salt);
		return passAndSaltBytes;
	}
	
	public List<Location> employee_authorized_location_sel(int companyid, int employeeid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("employee_authorized_location_sel"), new Location(), employeeid);
	}
	
	public List<WMUser> employee_sel_all_pos(int companyid, int locationid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("employee_sel_all_pos"), new WMUser(), locationid);
	}
	
	public List<WMUser> employee_sel_all_basic_location(int companyid, int locationid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("employee_sel_all_basic_location"), new WMUser(), locationid);
	}
	
	public List<WMUser> employee_sel_all_pin_basic_location(int companyid, int locationid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("employee_sel_all_pin_basic_location"), new WMUser(), locationid);
	}
	
	public List<WMUser> employee_sel_all_basic_no_position(int companyid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("employee_sel_all_basic_no_position"), new WMUser());
	}
	
	public void employee_authorized_location_ins(int companyid, int employeeid, int locationid) throws DuplicateException {
		DBUtils.runSPHelper(companyid, spProps.getProperty("employee_authorized_location_ins"), employeeid, locationid);
	}
	
	public void employee_authorized_location_del_all_employee(int companyid, int employeeid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("employee_authorized_location_del_all_employee"), employeeid);
	}
	
	public void employee_permission_del_all_employee(int companyid, int employeeid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("employee_permission_del_all_employee"), employeeid);
	}
	
	public void employee_permission_ins(int companyid, int employeeid, int permissionid) throws DuplicateException {
		DBUtils.runSPHelper(companyid, spProps.getProperty("employee_permission_ins"), employeeid, permissionid);
	}
	
	public List<Permission> employee_permission_sel(int companyid, int id) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("employee_permission_sel"), new Permission(), id);
	}
	
	/* *** MODULES *** */	
	public List<Module> employee_module_sel(int companyid, int employeeid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("employee_module_sel"), new Module(), employeeid);
	}
	
	public List<SubModule> employee_sub_module_sel(int companyid, int employeeid, int moduleid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("employee_sub_module_sel"), new SubModule(), employeeid, moduleid);
	}
	
	public void employee_sub_module_del_all_employee(int companyid, int employeeid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("employee_sub_module_del_all_employee"), employeeid);
	}
	
	public void employee_sub_module_ins(int companyid, int employeeid, int submoduleid, boolean sel, boolean ins, boolean upd, boolean del) throws DuplicateException {
		DBUtils.runSPHelper(companyid, spProps.getProperty("employee_sub_module_ins"), employeeid, submoduleid, sel, ins, upd, del);
	}
	/* *** END MODULES *** */
	
	/* *** SETTINGS *** */
	public void employee_settings_ins(int companyid, int employeeid, int defaultPosModuleId, int currentLocationId) throws DuplicateException {
		DBUtils.runSPHelper(companyid, spProps.getProperty("employee_settings_ins"), employeeid, (defaultPosModuleId == 0 ? null : defaultPosModuleId), currentLocationId);
	}
	
	public void employee_settings_upd(int companyid, int employeeid, String themePath, int defaultPosSubModuleId, int currentLocationId) {		
		DBUtils.runSPHelper(companyid, spProps.getProperty("employee_settings_upd"), employeeid, themePath, defaultPosSubModuleId, currentLocationId);
	}
	
	public UserSettings employee_settings_sel(int companyid, int employeeid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("employee_settings_sel"), new UserSettings(), employeeid);
	}	
	/* *** END SETTINGS *** */
}
