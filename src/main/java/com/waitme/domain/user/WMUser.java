package com.waitme.domain.user;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waitme.config.Constants;
import com.waitme.config.WMProperties;
import com.waitme.domain.WMDomainObject;
import com.waitme.domain.restaurant.Company;
import com.waitme.domain.restaurant.IActivatable;
import com.waitme.domain.restaurant.Location;
import com.waitme.exception.NoAuthorizedLocationsException;
import com.waitme.utils.FileUtils;
import com.waitme.utils.SecurityUtils;

/**
 * Class to represent a user of the application, usually employees
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-14
 * @since 1.0 2019-01-08
 */
public class WMUser extends WMDomainObject implements IActivatable, RowMapper<WMUser> {
	private int id;
	private Company company;
	private String uname, fname, lname, email, phone, iconPath;
	private PostalAddress address;
	private boolean loggedin, loggedinPos, active;
	private String loginToken, posLoginToken;
	private Position position;
	private List<Permission> permissions;
	private List<Module> modules;
	private List<Location> authorizedLocations;
	private UserSettings userSettings;
	private BigDecimal payAdjustment;
	private String pinHash512, pinHash256;
	private TimeSheet currentTimeSheet;
	
	private Map<String,SubModule> subModuleMap; //used for quick check of module permissions
	
	@JsonIgnore
	Properties props = new WMProperties("user.properties");;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(WMUser.class);
	
	public WMUser() {}
	
	//This is to create a simple user whose info can be populated from DB with id
	public WMUser(int id, String uname, String fname, String lname) {
		this(id, null, uname, fname, lname, "", "", null, null, new BigDecimal(0.0), true);
	}
	
	public WMUser(int id, Company company, String uname, String fname, String lname, String email, String phone, String address, UserSettings userSettings, BigDecimal payAdjustment, boolean active) {
		this.id = id;
		this.company = company;
		this.uname = uname;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.phone = phone;
		this.address = new PostalAddress(address);
		this.loggedin = false;
		this.loggedinPos = false;
		this.loginToken = "";
		this.posLoginToken = "";
		this.userSettings = userSettings;
		this.payAdjustment = payAdjustment;

		defaultIconPath();			
	}
	
	/**
	 * Creates an empty user with empty objects down to the primitive level
	 * @return
	 */
	public static WMUser emptyInit() {
		WMUser emptyWmUser = new WMUser();
		return emptyWmUser;
	}
	
	private void defaultIconPath() {
		if (uname !=null && !uname.isEmpty()) {//only do this if a proper uname was given
			String settingsDirStr = Constants.BASE_PATH + props.getProperty("settings.path") + File.separator + uname;
			try {
				File settingsDir = new File(settingsDirStr);
				if (!settingsDir.exists()) //if this is a new user that was just created, create a new settings folder for them
					settingsDir.mkdir();
				FileFilter fileFilter = new WildcardFileFilter(props.getProperty("settings.icon") + ".*");
				File icon = settingsDir.listFiles(fileFilter)[0];
				this.iconPath = props.getProperty("settings.basepath") + File.separator + uname + File.separator + icon.getName();
			} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {//file not found so create default icon
				this.iconPath = props.getProperty("settings.basepath") + File.separator + uname + File.separator + props.getProperty("settings.icon") + "." + props.getProperty("settings.icon.default.ext");
				try {
					FileUtils.copyReplaceFile(Constants.BASE_PATH + File.separator + props.getProperty("settings.icon.default"), settingsDirStr + File.separator + props.getProperty("settings.icon") + "." + props.getProperty("settings.icon.default.ext"));
				} catch (Exception e1) {
					log.error("Error while copying and replacing the file.");
					log.error(e1.getMessage());
				}
			}
		}
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getUname() {
		return uname;
	}
	
	public void setUname(String uname) {
		this.uname = uname;
//		defaultIconPath();
	}
	
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public PostalAddress getAddress() {
		return address;
	}

	public void setAddress(PostalAddress address) {
		this.address = address;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isLoggedin() {
		if (loginToken == null || loginToken.isEmpty())
			return false;
		return loggedin;
	}

	/**
	 * Logs user in and returns a random salt 
	 * to be used as validation for login in a cookie
	 * @param loggedin whether to login or out
	 * @return the unique and random login token
	 */
	public String setLoggedin(boolean loggedin) {
		if (loggedin)
			loginToken = new String(SecurityUtils.genSalt());
		else
			loginToken = "";
		this.loggedin = loggedin;
		return loginToken;
	}

	public boolean isLoggedinPos() {
		if (posLoginToken == null || posLoginToken.isEmpty())
			return false;
		return loggedinPos;
	}

	/**
	 * Logs user into POS and returns a random salt 
	 * to be used as validation for login in a cookie
	 * @param loggedin whether to login or out
	 * @return the unique and random login token
	 */
	public String setLoggedinPos(boolean loggedinPos) {
		if (loggedinPos)
			posLoginToken = new String(SecurityUtils.genSalt());
		else
			posLoginToken = "";
		this.loggedinPos = loggedinPos;
		return posLoginToken;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public String getPosLoginToken() {
		return posLoginToken;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	
	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
		
		if (modules != null) {
			//init submodules map
			subModuleMap = new HashMap<String, SubModule>();
			for (Module module : modules) {
				for (SubModule subModule : module.getSubModules()) {
					subModuleMap.put(subModule.getName().replaceAll("\\s+", ""), subModule);
				}
			}
		}
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}


	public Location getAuthorizedLocationById(int id) throws NoAuthorizedLocationsException {
		for (Location l : authorizedLocations) {
			if (l.getId() == id)
				return l;
		}
		throw new NoAuthorizedLocationsException("This user does not have access to location: " + id);
	}
	
	public List<Location> getAuthorizedLocations() {
		return authorizedLocations;
	}

	public void setAuthorizedLocations(List<Location> authorizedLocations) {
		this.authorizedLocations = authorizedLocations;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public UserSettings getUserSettings() {
		return userSettings;
	}

	public void setUserSettings(UserSettings userSettings) {
		this.userSettings = userSettings;
	}

	public BigDecimal getPayAdjustment() {
		return payAdjustment;
	}

	public void setPayAdjustment(BigDecimal payAdjustment) {
		this.payAdjustment = payAdjustment;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPinHash512() {
		return pinHash512;
	}
	
	public String getPinHash256() {
		return pinHash256;
	}

	public void setPin(int pin) {
		this.pinHash512 = SecurityUtils.hashInt512(pin);
		this.pinHash256 = SecurityUtils.hashInt256(pin);
	}
	
	public void clearPin512() {
		this.pinHash512 = null;
	}
	
	public void clearPin256() {
		this.pinHash256 = null;
	}
	
	public Map<String, SubModule> getSubModuleMap() {
		return subModuleMap;
	}

	public void setSubModuleMap(Map<String, SubModule> subModuleMap) {
		this.subModuleMap = subModuleMap;
	}
	
	public TimeSheet getCurrentTimeSheet() {
		return currentTimeSheet;
	}

	public void setCurrentTimeSheet(TimeSheet currentTimeSheet) {
		this.currentTimeSheet = currentTimeSheet;
	}
	
	public void deleteSettingsFolder() {
		String settingsDirStr = Constants.BASE_PATH + props.getProperty("settings.path") + File.separator + uname;
		File settingsDir = new File(settingsDirStr);
		try {
			org.apache.commons.io.FileUtils.deleteDirectory(settingsDir);
		} catch (IOException e) {
			log.error("Shouldn't happen because settings folder always exists for user.");
		}
	}
	
	@JsonIgnore
	private boolean clockedIn;
	
	@JsonProperty
	public boolean isClockedIn() {
		if (currentTimeSheet != null && currentTimeSheet.getTimeIn() != null && currentTimeSheet.getTimeOut() == null)
			return true;
		return false;
	}
	
	@JsonIgnore
	public void setClockedIn() {} //empty for json
	
	@Override
	public WMUser mapRow(ResultSet rs, int rowNum) throws SQLException {
		WMUser user = new WMUser();
		
		//user
		try {user.setId(rs.getInt("employee_id"));} catch(SQLException e) {throw new SQLException("No id for user. Not found.");}
		try {user.setUname(rs.getString("employee_uname"));} catch(SQLException e) {log.debug("No uname for user '" + user.getId() + "'");}
		try {user.setFname(rs.getString("employee_fname"));} catch(SQLException e) {log.debug("No fname for user '" + user.getUname() + "'");}
		try {user.setLname(rs.getString("employee_lname"));} catch(SQLException e) {log.debug("No lname for user '" + user.getUname() + "'");}
		try {user.setEmail(rs.getString("employee_email"));} catch(SQLException e) {log.debug("No email for user '" + user.getUname() + "'");}
		try {user.setPhone(rs.getString("employee_phone"));} catch(SQLException e) {log.debug("No phone for user '" + user.getUname() + "'");}
		try {user.setAddress(new PostalAddress(rs.getString("employee_address")));} catch(SQLException e) {log.debug("No email for user '" + user.getUname() + "'");}
		try {user.setPayAdjustment(rs.getBigDecimal("employee_pay_adjustment"));} catch(SQLException e) {log.debug("No pay_adjustment for user '" + user.getUname() + "'");}
		try {user.setActive(rs.getBoolean("employee_active"));} catch(SQLException e) {log.debug("No active for user '" + user.getUname() + "'");}
		try {user.setPin(rs.getInt("employee_pin"));} catch(SQLException e) {log.debug("No pin for user '" + user.getUname() + "'");}
		
		//settings
		try {
			rs.getString("employee_settings_theme_path"); //to check if info is available. If it is, use mapper
			UserSettings set = new UserSettings();
			set = set.mapRow(rs, rowNum);
			user.setUserSettings(set);
		} catch(SQLException e) {log.debug("No complete settings info for user '" + user.getUname() + "'");}
		
		//position
		try {
			int i = rs.getInt("position_role_id"); //to check if info is available. If it is, use mapper
			if (i == 0) {throw new SQLException();}
			Position pos = new Position();
			pos = pos.mapRow(rs, rowNum);
			user.setPosition(pos);
		} catch(SQLException e) {log.debug("No complete position info for user '" + user.getUname() + "'");}
		
		//company
		try {
			int i = rs.getInt("company_id"); //to check if info is available. If it is, use mapper
			if (i == 0) {throw new SQLException();}
			Company c = new Company();
			c = c.mapRow(rs, rowNum);
			user.setCompany(c);
		} catch(SQLException e) {log.debug("No complete company info for user '" + user.getUname() + "'");}
		
		//timesheet
		try {
			int i = rs.getInt("timesheet_day_id"); //to check if info is available. If it is, use mapper
			if (i == 0) {throw new SQLException();}
			TimeSheet t = new TimeSheet();
			t = t.mapRow(rs, rowNum);
			user.setCurrentTimeSheet(t);
		} catch(SQLException e) {log.debug("No complete timsheet info for user '" + user.getUname() + "'");}
		
		//final setup
//		user.setIconPath("/usr/" + user.getUname() + "/icon.png");
		user.defaultIconPath();
		
		return user;
	}
}
