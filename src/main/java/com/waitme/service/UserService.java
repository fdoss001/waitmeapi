package com.waitme.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.login.FailedLoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.waitme.domain.restaurant.Company;
import com.waitme.domain.restaurant.Location;
import com.waitme.config.Constants;
import com.waitme.config.WMProperties;
import com.waitme.domain.user.AjaxModule;
import com.waitme.domain.user.EPermission;
import com.waitme.domain.user.Module;
import com.waitme.domain.user.Permission;
import com.waitme.domain.user.SubModule;
import com.waitme.domain.user.WMUser;
import com.waitme.exception.AuthenticationException;
import com.waitme.exception.ClockException;
import com.waitme.exception.DeleteException;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoAuthorizedLocationsException;
import com.waitme.exception.NoAuthorizedModulesException;
import com.waitme.exception.NoCompanyException;
import com.waitme.exception.NoPermissionsException;
import com.waitme.domain.user.TimeSheet;
import com.waitme.domain.user.UserSettings;
import com.waitme.persistence.AdminDAO;
import com.waitme.persistence.UserDAO;
import com.waitme.utils.SecurityUtils;

import com.waitme.exception.NoResultException;
import com.waitme.exception.NoTimesheetException;
import com.waitme.exception.NoUserException;

/**
 * Service class for user related functions
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-19
 * @since 1.0 2019-02-08
 */
@Service
public class UserService {
	@Autowired
	UserDAO userDAO;
	@Autowired
	AdminDAO adminDAO;
	@Autowired
	AdminService adminService;
	@Autowired
	CompanyService companyService;
	private Logger log = LoggerFactory.getLogger(UserService.class);
	private Properties messageProps;
	
	private Map<Integer, String> loggedInUsers;
	private Map<Integer, String> loggedInPosUsers;
	
	public UserService() {
		/*
		 * these hashmaps will have logged in users to make sure
		 * they can't be logged in on multiple machines 
		 */
		loggedInUsers = new HashMap<Integer, String>();
		loggedInPosUsers = new HashMap<Integer, String>();
		messageProps = new WMProperties("messages.properties");
	}
	
	//This is only used for testing purposes
	public UserService(UserDAO ud, AdminDAO ad) {
		userDAO = ud;
		adminDAO = ad;
	}
	
	public Map<Integer, String> getLoggedInUsers() {
		return loggedInUsers;
	}
	
	public Map<Integer, String> getLoggedInPosUsers() {
		return loggedInPosUsers;
	}

	/**
	 * Create a new user in the DB
	 * @param wmUser the new user to create
	 * @param passwd the new password for the user
	 * @param pin the new pin for the user's POS. (must be unique!)
	 * @param creatorid the id of the user creating this new user
	 * @return the newly created user
	 * @throws DuplicateException if username or pin already exists
	 */
	public WMUser createNewWMUser(int companyid, WMUser wmUser, String passwd, int pin, int creatorid) throws DuplicateException, NoCompanyException {
		log.debug("Creating a new user '" + wmUser.getUname() + "' in the DB.");
		log.debug("Hasing password with salt.");
		//gen salt and hash the password first
		byte[] salt = SecurityUtils.genSalt();
		byte[] hashedPasswd = SecurityUtils.hashPassword(salt, passwd);
		
		//check for duplicate pin
		checkDuplicatePin(companyid, pin); //if it succeeds it will continue, otherwise it will throw exception
		

		//get the company for the user
		Company company = null;
		try {
			company = companyService.getCompany(companyid);
		} catch (NoResultException e) {
			throw new NoCompanyException(messageProps.getProperty("error.company_not_found"));
		}
		
		//Insert the user
		int id = 0;
		try {
			id = userDAO.employee_ins(companyid, wmUser.getUname(), wmUser.getFname(), wmUser.getLname(), wmUser.getEmail(), wmUser.getPhone(), wmUser.getAddress().serialize(), hashedPasswd, salt, pin, (wmUser.getPosition() == null ? 0 : wmUser.getPosition().getId()), wmUser.getPayAdjustment(), creatorid);
			wmUser.setId(id);
			wmUser.setCompany(company);
		} catch (DuplicateException e) {
			log.error("Duplicate username '" + wmUser.getUname() + "'. Cannot create user.");
			throw new DuplicateException(messageProps.getProperty("error.duplicate_username"));
		}
		
		if (wmUser.getPosition() == null) {//new waitme account so grant all permissions and modules
			wmUser.setPermissions(adminService.getAllPermissions());
			wmUser.setModules(adminService.getAllModulesWithPriveleges());
			//no locations have been made yet so no need to add any
		}
		
		//insert permissions
		for (Permission p : wmUser.getPermissions()) {
			log.debug("Inserting permission '" + p.getName() + "'");
			userDAO.employee_permission_ins(companyid, id, p.getId());
		}
		//insert authorized locations
		for (Location l : wmUser.getAuthorizedLocations()) {
			log.debug("Inserting authorized location '" + l.getName() + "'");
			userDAO.employee_authorized_location_ins(companyid, id, l.getId());
		}
		//insert modules
		for (Module m : wmUser.getModules()) {
			for (SubModule sm : m.getSubModules()) {
				log.debug("Inserting sub module '" + sm.getName() + "' from module '" + m.getName() + "'");
				userDAO.employee_sub_module_ins(companyid, id, sm.getId(), sm.isSel(), sm.isIns(), sm.isUpd(), sm.isDel());
			}
		}
		//insert settings
		log.debug("Inserting settings");
		userDAO.employee_settings_ins(companyid, id, (wmUser.getUserSettings() == null ? 0 : wmUser.getUserSettings().getDefaultPosSubModuleId()));
		//no need to create a settings folder because this is done from the WMUser constructor

		log.debug("Successfully created user");
		return wmUser;
	}
	
	/**
	 * Permanently deletes the user from the database.
	 * All corresponding settings, permissions, and timesheets are also deleted.
	 * WARNING! Use with extreme caution. Deactivating an employee is usually preferred.
	 * @param wmUser the user to delete
	 * @throws DeleteException if the employee cannot be deleted due to the employee having party history
	 */
	public void deleteWMUser(WMUser wmUser) throws DeleteException {		
		try {
			log.debug("Permanently deleting employee '" + wmUser.getUname() + "' and all their related objects.");
			userDAO.employee_del(wmUser.getCompany().getId(), wmUser.getId());
		} catch (DeleteException e) {
			log.error("Could not delete due to constraints. All required related objects must be deleted first.");
			throw new DeleteException("error;" + e.getMessage() + " Please delete before proceeding.");
		}
		log.debug("Deleting settings folder and icon.");
		wmUser.deleteSettingsFolder();
		log.debug("Successfully deleted user.");
	}
	
	/**
	 * Updates a user's password
	 * @param wmUser user to update password for
	 * @param newPasswd new password to use
	 */
	public void updateWMUserPasswd(WMUser wmUser, String newPasswd) {
		log.debug("Updating password for user '" + wmUser.getUname() + "' with a new salt.");
		//gen salt and hash the password first
		byte[] salt = SecurityUtils.genSalt();
		byte[] hashedPasswd = SecurityUtils.hashPassword(salt, newPasswd);
		
		userDAO.employee_upd_pwd(wmUser.getCompany().getId(), wmUser.getId(), hashedPasswd, salt);
		log.debug("Successfully updated password.");
	}
	
	/**
	 * Updates user pin
	 * @param wmUser user to update pin for
	 * @param newPin new pin to use (must be unique!)
	 * @throws DuplicateException if pin is already in use 
	 */
	public void updateWMUserPin(WMUser wmUser, int newPin) throws DuplicateException {
		try {
			log.debug("Updating pin for user '" + wmUser.getUname() + "'");
			userDAO.employee_upd_pin(wmUser.getCompany().getId(), wmUser.getId(), newPin);
		} catch (DuplicateException e) {
			log.error("Duplicate pin. Cannot update pin.");
			throw new DuplicateException(messageProps.getProperty("error.duplicate_pin"));
		}
		log.debug("Successfully updated pin.");
	}
	
	/**
	 * Updates the modules a user has access to
	 * @param wmUser the user to update
	 */
	public void updateWMUserModules(WMUser wmUser) {
		log.debug("Updating authorized modules for user '" + wmUser.getUname() + "'");
		userDAO.employee_sub_module_del_all_employee(wmUser.getCompany().getId(), wmUser.getId());
		for (Module m : wmUser.getModules()) {
			for (SubModule sm : m.getSubModules()) {
				log.debug("\tGranting submodule '" + sm.getName() + "' from module '" + m.getName() + "'");
				try {
					userDAO.employee_sub_module_ins(wmUser.getCompany().getId(), wmUser.getId(), sm.getId(), sm.isSel(), sm.isIns(), sm.isUpd(), sm.isDel());
				} catch (DuplicateException e) {
					log.warn("User already has acess to this submodule '" + sm.getName() + "'. This should not happen. Continuing.");	
				}
			}
		}
		log.debug("Successfully updated user's authorized modules.");
	}
	
	/**
	 * Updates the basic info for a user
	 * @param wmUser the user to update
	 * @param updaterId the id of the user updating this user
	 * @throws DuplicateException if a duplicate username was chosen
	 */
	public void updateWMUserBasicInfo(WMUser wmUser, int updaterId) throws DuplicateException {
		try {
			log.debug("Updating basic info for user '" + wmUser.getUname() + "'");
			userDAO.employee_upd_basic(wmUser.getCompany().getId(), wmUser.getId(), wmUser.getUname(), wmUser.getFname(), wmUser.getLname(), wmUser.getEmail(), wmUser.getPhone(), wmUser.getAddress().serialize(), updaterId);
		} catch (DuplicateException e) {
			log.error("Duplicate username '" + wmUser.getUname() + "'. Cannot update user.");
			throw new DuplicateException(messageProps.getProperty("error.duplicate_username"));
		}
		log.debug("Successfully updated user's basic info.");
	}
	
	/**
	 * Updates the user's position in the DB
	 * @param wmUser the user to update
	 * @param updaterId the user updating it
	 */
	public void updateWMUserPosition(WMUser wmUser, int updaterId) {
		log.debug("Updating position for user '" + wmUser.getUname() + "' to '" + wmUser.getPosition().getName() + "'");
		userDAO.employee_upd_position(wmUser.getCompany().getId(), wmUser.getId(), wmUser.getPosition().getId(), wmUser.getPayAdjustment(), updaterId);
		log.debug("Successfully updated user's position.");
	}
	
	/**
	 * Updates the settings for this user in the db
	 * @param wmUser the user to update
	 * @param userSettings the new settings for the user
	 */
	public void updateWMUserSettings(WMUser wmUser, UserSettings userSettings) {
		log.debug("Updating settings for user '" + wmUser.getUname() + "'");
		wmUser.setUserSettings(userSettings);
		userDAO.employee_settings_upd(wmUser.getCompany().getId(), wmUser.getId(), userSettings.getThemePath(), userSettings.getDefaultPosSubModuleId());
		log.debug("Successfully updated user's settings.");
	}
	
	/**
	 * Updates the permissions for the user in the DB
	 * @param wmUser the user to update
	 * @param permissions the new permissions to grant
	 */
	public void updateWMUserPermissions(WMUser wmUser, List<Permission> permissions) {
		log.debug("Updating permissions for user '" + wmUser.getUname() + "'");
		wmUser.setPermissions(permissions);
		userDAO.employee_permission_del_all_employee(wmUser.getCompany().getId(), wmUser.getId());
		for (Permission p : permissions) {
			log.debug("\tGranting permission '" + p.getName() + "'");
			try {
				userDAO.employee_permission_ins(wmUser.getCompany().getId(), wmUser.getId(), p.getId());
			} catch (DuplicateException e) {
				log.warn("User already has acess to this permission '" + p.getName() + "'. This should not happen. Continuing.");
			}			
		}
		log.debug("Successfully updated user's permissions.");
	}
	
	/**
	 * Updates the authorized locations for the user in the DB
	 * @param wmUser the user to update
	 * @param locations the new locations to authorize
	 */
	public void updateWMUserAuthorizedLocations(WMUser wmUser, List<Location> locations) {
		log.debug("Updating authorized locations for user '" + wmUser.getUname() + "'");
		wmUser.setAuthorizedLocations(locations);
		userDAO.employee_authorized_location_del_all_employee(wmUser.getCompany().getId(), wmUser.getId());
		for (Location l : locations) {
			log.debug("\tGranting location '" + l.getName() + "'");
			try {
				userDAO.employee_authorized_location_ins(wmUser.getCompany().getId(), wmUser.getId(), l.getId());
			} catch (DuplicateException e) {
				log.warn("User already has acess to this location '" + l.getName() + "'. This should not happen. Continuing.");
			}
		}
		log.debug("Successfully updated user's authorized locations.");
	}
	
	/**
	 * Gets the user by username
	 * @param un username
	 * @return the user
	 * @throws NoUserException if user with given username not found
	 */
	public WMUser getWMUser(String un) throws NoUserException {
		return getWMUserHelper(0, un);
	}
	
	/**
	 * Gets the user by pin (which is unique)
	 * @param pin the pin to select by
	 * @return the user
	 * @throws NoUserException if user with given pin not found
	 */
	public WMUser getWMUser(int companyid, int pin) throws NoUserException {
		return getWMUserHelper(companyid, pin);
	}
	
	/**
	 * Gets the user by id
	 * @param employeeid the employeeid to select by
	 * @return the user
	 * @throws NoUserException if user with given id not found
	 */
	public WMUser getWMUserById(int companyid, int employeeid) throws NoUserException {
		return getWMUserHelper(companyid, employeeid);
	}
	
	/**
	 * A helper for selecting a user from the DB.
	 * All get complete user methods above call this helper.
	 * The functionality after the stored procedure is executed is the same
	 * @param pk the primary key to select on (must be unique). This determines the stored procedure to be called
	 * @return the user
	 * @throws NoUserException if user with given key was not found
	 */
	private WMUser getWMUserHelper(int companyid, Object pk) throws NoUserException {
		WMUser user = null;
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String callingMethod = stackTraceElements[2].getMethodName();
		try {
			if (callingMethod.equals("getWMUserById")) {
				//TODO getting the calling method name is not the best way to do this
				log.debug("Getting user by id '" + pk + "'");
				user = userDAO.employee_sel_id(companyid, (int) pk);
			}
			else {
				switch (pk.getClass().getSimpleName()) {
				case "String":
					log.debug("Getting user by username '" + pk + "'");
					user = userDAO.employee_sel((String) pk);
					break;
				case "Integer":
					log.debug("Getting user by pin");
					user = userDAO.employee_sel_pin(companyid, (int) pk);
					break;
				default: break;
				}
			}
		} catch (NoResultException e) {
			log.error("User is not found for key '" + pk + "'");
			throw new NoUserException(messageProps.getProperty("error.user_not_found"));
		}
		log.debug("Found user '" + user.getUname() + "'");
		
		if (user.getPosition() == null) { //no position. new account, give all permissions and modules
			log.debug("User '" + user.getUname() + "' has no position. Probably a new admin account.");
			user.setPermissions(adminService.getAllPermissions());
			user.setModules(adminService.getAllModulesWithPriveleges());
			try {
				user.setAuthorizedLocations(adminService.getAllMinimalLocations(user.getCompany().getId()));
			} catch (NoResultException ne) {
				log.debug("First time setup causes the below message");
				log.warn(ne.getMessage());
			}
			log.debug("Returning user with all permissions, modules, and locations");
			return user;
		}
		
		//Override their permission values with ones from DB
		List<Permission> permissions = new ArrayList<Permission>(0);
		try {
			permissions = getUserPermissions(user.getCompany().getId(), user.getId());
		} catch (NoPermissionsException e) {log.debug("User '" + user.getUname() + "' has no permissions");}
		user.setPermissions(permissions);
		
		//Get user's authorized locations
		List<Location> locations = new ArrayList<Location>(0);
		try {
			locations = getAuthorizedLocations(user.getCompany().getId(), user.getId());
		} catch (NoAuthorizedLocationsException e) {log.debug("User '" + user.getUname() + "' has no authorized locations");}
		//if the user doesn't have their location under authorized, always add it
		if (!locations.contains(user.getPosition().getLocation())) {
			log.debug("User '" + user.getUname() + "' did not have their position's location as an authorized. Adding it now.");
			locations.add(user.getPosition().getLocation());
		}
		user.setAuthorizedLocations(locations);
		
		//Get user's authorized modules
		List<Module> modules = new ArrayList<Module>(0);
		try {
			modules = getUserModules(user.getCompany().getId(), user.getId());
		} catch (NoAuthorizedModulesException e) {log.debug("User '" + user.getUname() + "' has no authorized modules");}
		user.setModules(modules);
		
		log.debug("Successfully got the user.");
		return user;
	}
	
	/**
	 * Gets the user's basic info by id
	 * @param companyid the companyid the employee belongs to
	 * @param employeeid the employeeid to select by
	 * @return the user
	 * @throws NoResultException if user with given id not found
	 */
	public WMUser getSimpleWMUser(int companyid, int employeeid) throws NoResultException {
		log.debug("Getting user by id '" + employeeid + "' for company '" + companyid + "'");
		WMUser user = userDAO.employee_sel_id(companyid, employeeid);
		log.debug("Found user '" + user.getUname() + "'");
		return user;
	}
	
	/**
	 * Gets the user's basic info by username
	 * @param userName the username to select by
	 * @return the user
	 * @throws NoResultException if user with given id not found
	 */
	public WMUser getSimpleWMUser(String userName) throws NoResultException {
		log.debug("Getting user by username '" + userName + "'");
		WMUser user = userDAO.employee_sel(userName);
		log.debug("Found user '" + user.getUname() + "'");
		return user;
	}
	
	/**
	 * Gets all the employees at the given location with the necessary info needed for POS
	 * @param location the location to get the employees for
	 * @return the list of all employees at the given location
	 */
	public List<WMUser> getAllPOSEmployees(int companyid, Location location) throws NoResultException {
		log.debug("Getting all users with relevant POS info only");
		List<WMUser> wmUsers = userDAO.employee_sel_all_pos(companyid, location.getId());
		
		for (WMUser user : wmUsers) {
			log.debug("\tFound '" + user.getUname() + "'. Getting permissions.");
			
			List<Permission> permissions = new ArrayList<Permission>(0);
			try {
				permissions = getUserPermissions(user.getCompany().getId(), user.getId());
			} catch (NoPermissionsException e) {log.info("User " + user.getUname() + " has no permissions");}
			user.setPermissions(permissions);
			
			//set pos modules
			log.debug("Getting POS modules.");
			try {
				List<SubModule> posSubModules = getUserSubModules(user.getCompany().getId(), user.getId(), Constants.POS.MODULE_ID);
				Module posModule = new Module(Constants.POS.MODULE_ID, Constants.POS.NAME, posSubModules);
				List<Module> modules = new ArrayList<Module>(1);
				modules.add(posModule);
				user.setModules(modules);
			} catch(NoAuthorizedModulesException e) {
				log.warn("User '" + user.getUname() + "' has no POS submodules. They will not be able to use the POS.");
			}
			
			log.debug("Finished getting with user '" + user.getUname() + "'");
		}
		
		log.debug("Successfully got all (" + wmUsers.size() + ") users with relevant POS information only.");
		return wmUsers;
	}
	
	/**
	 * Gets all the basic info for all users whose position is at the given location
	 * @param locationid the location to get employees for
	 * @return list of all users
	 * @throws NoResultException if the location has no users
	 */
	public List<WMUser> getAllWMUsersBasic(int companyid, int locationid) throws NoResultException {
		log.debug("Get all users with basic info only."); 
		List<WMUser> wmUsers;
		try {
			wmUsers = userDAO.employee_sel_all_basic_location(companyid, locationid);
		} catch (NoResultException e) {
			log.warn("This location '" + locationid + "' has no users.");
			throw new NoResultException("This location '" + locationid + "' has no users.");
		}
		log.debug("Successfully got all (" + wmUsers.size() + ") users with basic info only.");
		return wmUsers;		
	}
	
	/**
	 * Gets all the basic info for all users whose position is at the given location including the pin
	 * @param location the location to get employees for
	 * @return list of all users
	 */
	public List<WMUser> getAllWMUsersBasicPin(int companyid, Location location) throws NoResultException {
		log.debug("Getting all the user's hashed pins");
		List<WMUser> wmUsers = userDAO.employee_sel_all_pin_basic_location(companyid, location.getId()); //should never not found throw exception
		
		for (WMUser user : wmUsers) {
			log.debug("\tGot pin for user '" + user.getId() + "'. Clearing 256 hash.");
			user.clearPin256();
		}
		log.debug("Successfully got all (" + wmUsers.size() + ") the user's hashed pins");
		return wmUsers;		
	}
	
	/**
	 * Gets all the basic info for all that have no position
	 * @return list of all users
	 */
	public List<WMUser> getAllWMUsersBasicNoPosition(int companyid) throws NoResultException {
		log.debug("Get all basic info for users with no position. There shouldn't be any unless this is first time setup");
		List<WMUser> wmUsers = userDAO.employee_sel_all_basic_no_position(companyid);
		log.debug("Successfully got all (" + wmUsers.size() + ") basic info for users with no position. This most likely means this is a first time setup.");
		return wmUsers;		
	}
	
	/**
	 * This only logs user into POS so a master login was assumed.
	 * @param wmUser the user to log into POS
	 * @throws ClockException if user is not clocked in
	 */
	public void posLoginWMUser(WMUser wmUser) throws ClockException {
		try {
			log.debug("Attempting to login user '" + wmUser.getUname() + "' to the POS. The validation was done client side.");
			//execute DB login
			userDAO.employee_loginout_pos(wmUser.getCompany().getId(), wmUser.getId(), true);
		} catch (ClockException e) {
			log.error("The user is not clocked in. This should not happen since it is caught client side.");
			throw new ClockException(messageProps.getProperty("error.not_clocked_in"), 0);
		}
		//set user object login
		String token = wmUser.setLoggedinPos(true);
		log.debug("Successfully logged in to POS. Added unique POS token '" + token + "' for user '" + wmUser.getUname() + "'. "
				+ "This will prevent multiple logins on different machines as long as the machines are online.");
		loggedInPosUsers.remove(wmUser.getId()); //remove old token from server
		loggedInPosUsers.put(wmUser.getId(), token);
	}
	
	/**
	 * Logs out the user from POS
	 * @param wmUser user to log out
	 */
	public void posLogoutWMUser(WMUser wmUser) {
		log.debug("Logging out user '" + wmUser.getUname() + "' from POS.");
		//execute DB logout
		try {
			userDAO.employee_loginout_pos(wmUser.getCompany().getId(), wmUser.getId(), false);
		} catch (ClockException e) {
			log.error("This should never happen. Clock exception is never thrown on trying to logout.");
		}	
		//set user object login
		wmUser.setLoggedinPos(false);
		loggedInPosUsers.remove(wmUser.getId());
		log.debug("Successfully logged out from POS. Also removed POS token.");
	}
	
	/**
	 * Clocks in the user creating a timesheet entry
	 * @param wmUser user to clock in
	 * @throws ClockException if done out of order or invalidly
	 */
	public void posClockIn(WMUser wmUser) throws ClockException {
		try {
			log.debug("Clocking in for user '" + wmUser.getUname() + "'");
			userDAO.employee_clockin(wmUser.getCompany().getId(), wmUser.getId(), wmUser.getCurrentTimeSheet().getLocationId(), wmUser.getCurrentTimeSheet().getStartOfWeek(), wmUser.getCurrentTimeSheet().getTimeIn());
		} catch (ClockException e) {
			log.error("The user '" + wmUser.getUname() + "' is already clocked in. This shouldn't happen since it is caught client side.");
			throw new ClockException(messageProps.getProperty("error.already_clocked_in"), 0);
		}
		log.debug("Successfully clocked in. This will be reflected on the timesheet.");
	}
	
	/**
	 * Clocks out a user updating their timesheet for the day
	 * @param wmUser user to clock out
	 * @throws ClockException if done out of order or invalidly
	 */
	public void posClockOut(WMUser wmUser) throws ClockException {
		try {
			log.debug("Clocking out for user '" + wmUser.getUname() + "'");
			userDAO.employee_clockout(wmUser.getCompany().getId(), wmUser.getId(), wmUser.getCurrentTimeSheet().getTimeOut());
		} catch (ClockException e) {
			log.error("The user '" + wmUser.getUname() + "' is not yet clocked in. This shouldn't happen since it is caught client side.");
			throw new ClockException(messageProps.getProperty("error.not_clocked_in"), 0);
		}
		log.debug("Successfully clocked out. This will be reflected on the timesheet.");
	}

	/**
	 * Starts a user's break updating their timesheet for the day
	 * @param wmUser user to start break
	 * @throws ClockException if done out of order or invalidly
	 */
	public void posBreakStart(WMUser wmUser) throws ClockException {
		try {
			log.debug("Starting break for user '" + wmUser.getUname() + "'");
			userDAO.employee_break_start(wmUser.getCompany().getId(), wmUser.getId());
		} catch (ClockException e) {
			if (e.getCode() == 45001) {
				log.error("The user '" + wmUser.getUname() + "' is not yet clocked in. This shouldn't happen since it is caught client side.");
				throw new ClockException(messageProps.getProperty("error.not_clocked_in"), 0);
			}
			else if (e.getCode() == 45002) {
				log.error("The user '" + wmUser.getUname() + "' has already started their break . This shouldn't happen since it is caught client side.");
				throw new ClockException(messageProps.getProperty("error.already_started_break"), 0);
			}
		}
		log.debug("Successfully started break. This will be reflected on the timesheet.");
	}
	

	/**
	 * Ends a user's break updating their timesheet for the day
	 * @param wmUser user to end break
	 * @throws ClockException if done out of order or invalidly
	 */
	public void posBreakEnd(WMUser wmUser) throws ClockException {
		try {
			log.debug("Ending break for user '" + wmUser.getUname() + "'");
			userDAO.employee_break_end(wmUser.getCompany().getId(), wmUser.getId());
		} catch (ClockException e) {
			if (e.getCode() == 45001) {
				log.error("The user '" + wmUser.getUname() + "' is not yet clocked in. This shouldn't happen since it is caught client side.");
				throw new ClockException(messageProps.getProperty("error.not_clocked_in"), 0);
			}
			else if (e.getCode() == 45002) {
				log.error("The user '" + wmUser.getUname() + "' has already ended their break. This shouldn't happen since it is caught client side.");
				throw new ClockException(messageProps.getProperty("error.already_ended_break"), 0);
			}
			else if (e.getCode() == 45003) {
				log.error("The user '" + wmUser.getUname() + "' has not yet started their break. This shouldn't happen since it is caught client side.");
				throw new ClockException(messageProps.getProperty("error.not_started_break"), 0);
			}
		}
		log.debug("Successfully ended break. This will be reflected on the timesheet.");
	}
	
	//For testing purposes. Do not use
//	public void passwordlessLogin(WMUser wmUser) {
//		//execute DB login
//		DBUtils.prepExecStatement(spProps.getProperty("employee_loginout"), false, wmUser.getId(), true);
//		//set user object login
//		wmUser.setLoggedin(true);
//	}
	
	/**
	 * Logs in a user to the application
	 * @param wmUser user to login
	 * @param pass user's password to validate against
	 * @throws FailedLoginException if credentials invalid
	 */
	public WMUser loginWMUser(WMUser wmUser, String pass) throws AuthenticationException {
		log.debug("Attempting to login user '" + wmUser.getUname() + "'");
		validateCredentials(wmUser, pass);
		//execute DB login
		log.debug("Credentials were good. Logging in");
		userDAO.employee_loginout(wmUser.getCompany().getId(), wmUser.getId(), true);
		//set user object login
		String token = wmUser.setLoggedin(true);
		log.debug("Successfully logged in. Added unique token '" + token + "' for user '" + wmUser.getUname() + "'. "
				+ "This will prevent multiple logins on different machines.");
		loggedInUsers.remove(wmUser.getId()); //remove old token from server
		loggedInUsers.put(wmUser.getId(), token);
		
		WMUser retUser = new WMUser(wmUser.getId(), wmUser.getUname(), wmUser.getFname(), wmUser.getLname());
		retUser.setCompany(new Company(wmUser.getCompany().getId()));
		retUser.setIconPath(wmUser.getIconPath());
		return retUser;
	}
	
	/**
	 * Validates a user's credentials by throwing exception if invalid
	 * @param wmUser user to validate credentials for
	 * @param passwd password to check against
	 * @throws FailedLoginException if password incorrect
	 */
	public void validateCredentials(WMUser wmUser, String passwd) throws AuthenticationException {
		//Get pass and salt from DB
		Map<String, byte[]> passAndSalt = null;
		try {
			log.debug("Validating credentials for user '" + wmUser.getUname() + "'");
			passAndSalt = userDAO.employee_sel_pwd(wmUser.getCompany().getId(), wmUser.getId()); //should never throw exception
		} catch (NoResultException e) {
			log.error("This should never happen as the given user is valid.");
		}
		//hash password before comparing		
		byte[] hashedPasswd = SecurityUtils.hashPassword(passAndSalt.get("employee_salt"), passwd);
		
		//validate password if logging in
		if (!Arrays.equals(passAndSalt.get("employee_passwd"), hashedPasswd))
			throw new AuthenticationException(messageProps.getProperty("error.password_incorrect"));
	}
	
	/**
	 * Logs out the user
	 * @param wmUser user to logout
	 */
	public void logoutWMUser(WMUser wmUser) {
		log.debug("Logging out user '" + wmUser.getUname() + "'");
		//execute DB logout
		userDAO.employee_loginout(wmUser.getCompany().getId(), wmUser.getId(), false);
		//set user object login
		wmUser.setLoggedin(false);	
		loggedInUsers.remove(wmUser.getId());
		log.debug("Successfully logged out. Also removed token.");
	}
	
	/**
	 * Validates if a user has a given permission
	 * @param wmUser user to check
	 * @param permission permission to check for
	 * @throws NoPermissionsException if permission is not found for user
	 */
	public void validatePermission(WMUser wmUser, EPermission permission) throws NoPermissionsException {
		log.debug("Checking if user '" + wmUser.getUname() + "' has permission '" + permission.toString() + "'");
		//validate the user has the permission
		if (wmUser.getPermissions().contains(new Permission(permission))) {
			log.debug("User has permission.");
			return;
		}
		throw new NoPermissionsException(messageProps.getProperty("error.insufficient_permissions"));
	}
	
	public void validateModulePermission(WMUser wmUser, String module, EPermission function) throws NoPermissionsException {
		log.debug("Checking module permission for user '" + wmUser.getUname() + "'");
		SubModule subModule = wmUser.getSubModuleMap().get(module);	
		if (subModule == null) {
			throw new NoPermissionsException(messageProps.getProperty("error.insufficient_permissions"));
		}

		log.debug("Checking if user has '" + function.toString() + "' permission for sub module '" + subModule.getName() + "'");
		boolean hasPermission = false;
		switch (function) {
		case sel: hasPermission = subModule.isSel(); break;
		case ins: hasPermission = subModule.isIns(); break;
		case upd: hasPermission = subModule.isUpd(); break;
		case del: hasPermission = subModule.isDel(); break;
		default: break;
		}
		if (!hasPermission)
			throw new NoPermissionsException(messageProps.getProperty("error.insufficient_permissions"));
		log.debug("User has permission.");
	}
	
	/**
	 * Gets all the timesheet objects for a given employee starting on the given week
	 * @param employee employee whose timesheet to retrieve
	 * @param startOfWeek week start to check for (beginning on Mondays) 
	 * @return the timesheet object retrieved
	 * @throws NoResultException if timesheet not found
	 */
	public List<TimeSheet> getTimeSheet(int companyid, int employeeid, Date startOfWeek) throws NoTimesheetException {	
		log.debug("Getting all timesheets for user '" + employeeid + "' with a weekstart of '" + startOfWeek + "'");
		List<TimeSheet> timeSheets = null;
		try {
			timeSheets = userDAO.timesheet_day_sel_all_week(companyid, employeeid, startOfWeek);
		} catch (NoResultException e) {
			throw new NoTimesheetException(messageProps.getProperty("warning.no_timesheet"));
		}
		
		log.debug("Succesfully got all timesheets for user '" + employeeid + "' with a weekstart of '" + startOfWeek + "'.\n"
				+ "Got '" + timeSheets.size() + "' timesheets.");
		return timeSheets;
	}
	
	/**
	 * Get the latest open timesheet object for a given employee starting on the given week
	 * @param employee employee whose timesheet to retrieve
	 * @param startOfWeek week start to check for (beginning on Mondays) 
	 * @return the timesheet object retrieved
	 * @throws NoResultException if timesheet not found
	 */
	public TimeSheet getCurrentOpenTimeSheet(int companyid, int employeeid) throws NoTimesheetException {
		TimeSheet timeSheet = null;
		try {
			log.debug("Getting current open timesheet for '" + employeeid + "'");
			timeSheet = userDAO.timesheet_day_sel_current_open(companyid, employeeid);
		} catch (NoResultException e) {
			throw new NoTimesheetException(messageProps.getProperty("warning.no_timesheet"));
		}
		log.debug("Got current open timesheet with weekstart '" + timeSheet.getStartOfWeek() + "' and clock in '" + timeSheet.getTimeIn() + "'");
		
		return timeSheet;
	}
	
	/**
	 * Gets all the authorized locations for an employee (location is minimal)
	 * @param employeeid the id of the employee to get the authorized locations for
	 * @return the list of location objects (minimal locations)
	 * @throws NoAuthorizedLocationsException if the employee has no authorized locations
	 */
	private List<Location> getAuthorizedLocations(int companyid, int employeeid) throws NoAuthorizedLocationsException {
		List<Location> locations;
		try {
			log.debug("Getting authorized locations for user '" + employeeid + "'");
			locations = userDAO.employee_authorized_location_sel(companyid, employeeid);
		} catch (NoResultException e) {
			throw new NoAuthorizedLocationsException("User has no authorized locations");
		}
		log.debug("Finished getting all authorized locations");
		
		return locations;
	}
	
	/**
	 * Gets all the modules a user has access to
	 * @param employeeid the id of the user to get the modules for
	 * @return the list of module objects
	 * @throws NoModulesException if the user has no modules accessible to them
	 */
	private List<Module> getUserModules(int companyid, int employeeid) throws NoAuthorizedModulesException {
		List<Module> modules;
		try {
			log.debug("Getting modules for user '" + employeeid + "'");
			modules = userDAO.employee_module_sel(companyid, employeeid);
			//now that we have the modules, let's get the submodules for the modules
			for (Module m : modules) {
				try {
					m.setSubModules(getUserSubModules(companyid, employeeid, m.getId()));
				} catch (NoAuthorizedModulesException e) { //user has not sub modules for module, set as empty list
					m.setSubModules(new ArrayList<SubModule>(0));
				}
				
			}
		} catch (NoResultException e1) {
			throw new NoAuthorizedModulesException("User has no sub modules");
		}
		log.debug("Finished getting all modules");
		
		return modules;
	}
	
	/**
	 * Gets all the modules a user has access to
	 * @param employeeid the id of the user to get the modules for
	 * @return the list of module objects
	 * @throws NoAuthorizedModulesException if the user has no sub modules accessible to them
	 */
	private List<SubModule> getUserSubModules(int companyid, int employeeid, int moduleid) throws NoAuthorizedModulesException {
		List<SubModule> subModules;
		try {
			log.debug("\tGetting submodules for module '" + moduleid + "'");
			subModules = userDAO.employee_sub_module_sel(companyid, employeeid, moduleid);
		} catch (NoResultException e) {
			throw new NoAuthorizedModulesException("User has no sub modules for module '" + moduleid + "'");
		}
		for (SubModule sm : subModules) {
			log.debug("\t\tFound submodule '" + sm.getName() + "'. Getting ajax modules");
			try {
				List<AjaxModule> ajaxModules = adminDAO.ajax_module_sub_module_sel((int) sm.getId());
				sm.setAjaxModules(ajaxModules);
				log.debug("\t\tAdded all ajax modules to submodule '" + sm.getName() + "'");
			} catch (NoResultException e) {
				log.debug("Sub module '" + sm.getName() + "' has no ajax modules. Continuing...");
			}
		}
		log.debug("Finished getting all modules");
		
		return subModules;
	}
	
	/**
	 * Gets all the permissions of a user
	 * @param companyid
	 * @param employeeid
	 * @return
	 * @throws NoPermissionsException
	 */
	private List<Permission> getUserPermissions(int companyid, int employeeid) throws NoPermissionsException {
		List<Permission> permissions;
		try {
			log.debug("Getting permissions for user '" + employeeid + "'");
			permissions = userDAO.employee_permission_sel(companyid, employeeid);
		} catch (NoResultException e) {
			throw new NoPermissionsException("User has no permissions");
		}
		log.debug("Finished getting all permissions");
		return permissions;
	}
	
	/**
	 * Checks the DB to see if this pin already exists
	 * @param pin the pin to check for
	 * @throws DuplicateException if the pin already exists
	 */
	private void checkDuplicatePin(int companyid, int pin) throws DuplicateException {
		int pinCheck = 0;
		try {
			log.debug("Checking for duplicate pin.");
			pinCheck = userDAO.employee_sel_pin_cnt(companyid, pin);
		} catch (NoResultException e) {log.error("Should never happen because select count always returns.");}
		if (pinCheck > 0)
			throw new DuplicateException(messageProps.getProperty("error.duplicate_pin"));
		else
			log.debug("no duplicate pin was found so continuing");
	}
}
