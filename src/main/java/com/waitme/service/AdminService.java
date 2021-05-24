package com.waitme.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.waitme.config.WMProperties;
import com.waitme.domain.restaurant.Location;
import com.waitme.domain.restaurant.Table;
import com.waitme.domain.user.Module;
import com.waitme.domain.user.Permission;
import com.waitme.domain.user.Position;
import com.waitme.domain.user.SubModule;
import com.waitme.domain.user.WMUser;
import com.waitme.exception.DeleteException;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoPositionException;
import com.waitme.persistence.AdminDAO;
import com.waitme.utils.WMLogger;
import com.waitme.exception.NoResultException;

/**
 * Service class for admin related functions
 * @author Fernando Dos Santos
 * @version 1.0 2019-04-01
 * @since 1.0 2019-04-01
 */
@Service
public class AdminService {
	@Autowired
	AdminDAO adminDAO;
	@Autowired
	RestaurantManagementService restaurantManagementService;
	Properties messageProps = new WMProperties("messages.properties");
	private WMLogger log = new WMLogger(AdminService.class);
	
	public AdminService(AdminDAO ad) {
		adminDAO = ad;
	}
	
	/* *** LOCATIONS *** */
	/**
	 * Creates a new location in the DB
	 * @param location the location to create
	 * @param creator the user who is creating this location
	 * @return the location object
	 * @throws DuplicateException if the location name already exists
	 */
	public Location createNewLocation(Location location, WMUser creator) throws DuplicateException {
		log.debug("Creating new location '" + location.getName() + "' in the DB.");
		int id = 0;
		try {
			id = adminDAO.location_ins(creator.getCompany().getId(), location.getName(), location.getType().toString(), location.getAddress().serialize(), location.getPhone(), location.getDateTimesOpen().toString(), creator.getId());
			location.setId(id);
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_name"));
		}
		log.debug("Successfully created location");
		return location;
	}
	
	/**
	 * Permanently deletes a location from the DB.
	 * WARNING! This should rarely be done. All history and positions must be deleted first
	 * Use with extreme caution. Deactivating a location is usually preferred. 
	 * @param locationid the id of the location to be deleted
	 * @throws DeleteException if the location cannot be deleted because it has party/order history, positions assigned, or timesheets under it.
	 */
	public void deleteLocation(int companyid, int locationid) throws DeleteException {
		log.debug("Permanently deleting location '" + locationid + "' and all their related objects.");
		try {
			adminDAO.location_del(companyid, locationid);
		} catch (DeleteException e) {
			log.error("Could not delete due to constraints. All required related objects must be deleted first.");
			throw new DeleteException("error;" + e.getMessage() + " Please delete before proceeding.");
		}
		log.debug("Successfully deleted location.");
	}
	
	/**
	 * Updates a location in the db
	 * @param location the location to update
	 * @param updater the user who is updating it
	 * @throws DuplicateException if the name is duplicated
	 */
	public void updateLocation(Location location, WMUser updater) throws DuplicateException {
		try {
			log.debug("Updating location '" + location.getId() + "'");
			adminDAO.location_upd(updater.getCompany().getId(), location.getId(), location.getName(), location.getType().toString(), location.getAddress().serialize(), location.getPhone(), location.getDateTimesOpen().toString(), updater.getId());
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_name"));
		}
		log.debug("Successfully updated location.");
	}
	
	

	/**
	 * Get minimal info about all locations. 
	 * This only includes id and name
	 * @return The list of all locations
	 * @throws NoResultException if no locations have been set up yet
	 */
	public List<Location> getAllMinimalLocations(int companyid) throws NoResultException {
		List<Location> locations;
		try {
			log.debug("Getting all locations with minimal info only.");
			locations = adminDAO.location_sel_all_minimal(companyid);
		} catch (NoResultException e) {
			log.debug("There are no locations. This should only happen on first time setup.");
			throw new NoResultException(messageProps.getProperty("error.no_locations"));
		}
		log.debug("Successfully got all (" + locations.size() + ") locations with minimal info only.");
		return locations;
	}
	
	/**
	 * Get simple info about all locations. 
	 * This does not include tables as it would be too large
	 * @return The list of all locations
	 * @throws NoResultException if no locations have been set up yet
	 */
	public List<Location> getAllSimpleLocations(int companyid) throws NoResultException {
		List<Location> locations;
		try {
			log.debug("Getting all locations with no table info.");
			locations = adminDAO.location_sel_all(companyid);
		} catch (NoResultException e) {
			log.debug("There are no locations. This should only happen on first time setup.");
			throw new NoResultException(messageProps.getProperty("error.no_locations"));
		}
		log.debug("Successfully got all (" + locations.size() + ") locations with no table info.");
		return locations;
	}
	
	/**
	 * Gets the location with only name and id
	 * @param locationid the id of the location to get
	 * @return the minimal location
	 * @throws NoResultException if the location with id doesn't exist
	 */
	public Location getMinimalLocation(int companyid, int locationid) throws NoResultException {
		log.debug("Getting minimal info for location id '" + locationid + "'");
		Location location = adminDAO.location_sel_minimal(companyid, locationid);
		log.debug("Got minimal info for location '" + location.getName() + "'");
		return location;
	}
	
	/**
	 * Gets the location object without table info
	 * @param locationid the id of the location to get
	 * @return the simple location
	 * @throws NoResultException if the location with id doesn't exist
	 */
	public Location getSimpleLocation(int companyid, int locationid) throws NoResultException {
		log.debug("Getting info without tables for location id '" + locationid + "'");
		Location location = adminDAO.location_sel(companyid, locationid);
		log.debug("Got info without tables for location '" + location.getName() + "'");
		return location;
	}
	
	/**
	 * Get the location with all the tables
	 * The tables will not have hosting details such as party or assigned employee
	 * @param locationid the id of the location to get
	 * @return the location object
	 * @throws NoResultException if the location with id doesn't exist
	 */
	public Location getLocation(int companyid, int locationid) throws NoResultException {		
		log.debug("Getting complete location for id '" + locationid + "'");
		List<Table> tables = restaurantManagementService.getTables(companyid, locationid);
		
		Location location = getSimpleLocation(companyid, locationid);
		location.setTables(tables);
		log.debug("Got complate location '" + location.getName() + "'. It has '" + tables.size() + "' tables.");
		return location;
	}
	
	
	/* *** POSITIONS *** */
	/**
	 * Gets the position object
	 * @param positionid the id of the position to get
	 * @return the position object
	 * @throws NoResultException if the position with id doesn't exist
	 */
	public Position getPosition(int companyid, int positionid) throws NoResultException {		
		log.debug("Getting position for id '" + positionid + "'");
		Position position = adminDAO.position_role_sel(companyid, positionid);
		log.debug("Got position '" + position.getName() + "'");
		return position;
	}
	
	/**
	 * Creates a new position in the DB
	 * @param position the position to create
	 * @param creator the user who is creating this position
	 * @return the new position with the correct id
	 */
	public Position createNewPosition(Position position, WMUser creator) throws DuplicateException {
		log.debug("Creating new position '" + position.getName() + "' in the DB.");
		int id = 0;
		try {
			id = adminDAO.position_role_ins(creator.getCompany().getId(), position.getCode(), position.getName(), position.getPayType().toString(), position.getBasePay(), position.getLocation().getId(), creator.getId());
			position.setId(id);
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));
		}
		log.debug("Successfully created position.");
		return position;
	}
	
	/**
	 * Permanently deletes a position from the DB.
	 * WARNING! This should rarely be done. All employees for this position must be deleted or reassigned first
	 * Use with extreme caution. Deactivating a position is usually preferred. 
	 * @param locationid the id of the position to be deleted
	 * @throws DeleteException if the position cannot be deleted because it has employees assigned
	 */
	public void deletePosition(int companyid, int positionid) throws DeleteException {		
		log.debug("Permanently deleting position '" + positionid + "' and all their related objects.");
		try {
			adminDAO.position_del(companyid, positionid);
		} catch (DeleteException e) {
			log.error("Could not delete due to constraints. All required related objects must be deleted or reassigned first.");
			throw new DeleteException("error;" + e.getMessage() + " Please delete or reassign before proceeding.");
		}
		log.debug("Successfully deleted position.");
	}
	
	/**
	 * Updates the position in the DB
	 * @param position the position to update
	 * @param updater the user who is updating it
	 */
	public void updatePosition(Position position, WMUser updater) throws DuplicateException {		
		try {
			log.debug("Updating position '" + position.getId() + "'");
			adminDAO.position_role_upd(updater.getCompany().getId(), position.getId(), position.getCode(), position.getName(), position.getPayType().toString(), position.getBasePay(), position.getLocation().getId(), updater.getId());
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));
		}
		log.debug("Successfully updated position.");
	}

	
	/**
	 * Gets all the positions from the DB with the minimal location
	 * @return a list of position objects
	 * @throws NoResultException if no positions have been setup yet
	 */
	public List<Position> getAllPositions(int companyid) throws NoPositionException {
		List<Position> positions;
		try {
			log.debug("Getting all positions.");
			positions = adminDAO.position_role_sel_all(companyid);
		} catch (NoResultException e) {
			throw new NoPositionException(messageProps.getProperty("error.no_positions"));
		}
		
		log.debug("Successfully got all (" + positions.size() + ") positions.");
		return positions;
	}
	
	
	/* *** PERMISSIONS & MODULES *** */
	/**
	 * Gets a list of all available permissions
	 * @return the list of all permissions
	 */
	public List<Permission> getAllPermissions() {
		log.debug("Getting all permissions.");
		List<Permission> permissions = null;
		try {
			permissions = adminDAO.permission_sel_all();
		} catch (NoResultException e) {log.error("Waitme always has permissions. This should never happen.");}
		log.debug("Successfully got all (" + permissions.size() + ") permissions.");
		return permissions;
	}
	
	/**
	 * Gets a list of all available modules
	 * @return the list of all modules
	 */
	public List<Module> getAllModules() {
		log.debug("Getting all modules.");
		List<Module> modules = null;
		try {
			modules = adminDAO.module_sel_all();
		} catch (NoResultException e) {log.error("Waitme always has modules. This should never happen.");}
		
		for (Module m : modules) {
			log.debug("\tFound module '" + m.getName() + "'. Getting all submodules for it.");
			List<SubModule> subModules = getSubModules(m.getId());
			m.setSubModules(subModules);
		}
		log.debug("Successfully got all (" + modules.size() + ") modules.");
		return modules;
	}
	
	/**
	 * Gets a list of all available modules with all privileges to sub modules
	 * @return the list of all modules
	 */
	public List<Module> getAllModulesWithPriveleges() {
		log.debug("Getting all modules.");
		List<Module> modules = null;
		try {
			modules = adminDAO.module_sel_all();
		} catch (NoResultException e) {log.error("Waitme always has modules. This should never happen.");}
		
		for (Module m : modules) {
			log.debug("\tFound module '" + m.getName() + "'. Getting all submodules for it.");
			List<SubModule> subModules = getSubModules(m.getId());
			for (SubModule sm : subModules) {
				sm.setSel(true);
				sm.setIns(true);
				sm.setUpd(true);
			}
			m.setSubModules(subModules);
		}
		log.debug("Successfully got all (" + modules.size() + ") modules.");
		return modules;
	}
	
	/**
	 * Gets all the submodules for a given module 
	 * @param moduleid the id of the module to fetch the submodules for
	 * @return the list of submodules for the module
	 */
	private List<SubModule> getSubModules(int moduleid) {
		log.debug("Getting all submodules for id '" + moduleid + "'");
		List<SubModule> subModules = null;
		try {
			subModules = adminDAO.sub_module_module_sel(moduleid);
		} catch (NoResultException e) {log.error("Waitme always has sub modules. This should never happen.");}		
		log.debug("\tSuccessfully got all (" + subModules.size() + ") sub modules for module with id '" + moduleid + "'");
		return subModules;
	}
}
