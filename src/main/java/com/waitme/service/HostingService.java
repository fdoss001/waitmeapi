package com.waitme.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.waitme.domain.restaurant.Guest;
import com.waitme.domain.restaurant.Location;
import com.waitme.domain.restaurant.Party;
import com.waitme.domain.restaurant.Table;
import com.waitme.domain.user.WMUser;
import com.waitme.persistence.HostingDAO;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoResultException;

/**
 * Service class for hosting related functions
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-20
 * @since 1.0 2019-02-20
 */
@Service
public class HostingService {
	@Autowired
	HostingDAO hostingDAO;
	
	Logger log = LoggerFactory.getLogger(HostingService.class);
	
	public HostingService() {}
	
	public HostingService(HostingDAO hd) {
		hostingDAO = hd;
	}
	
	/**
	 * Retrieves the tables for a given location and their related parties and guests if they exist
	 * Guests do not have orders, for that, use waiting service
	 * @param companyid the id of the company used to assign the connection string
	 * @param locationid the id of the location of the tables
	 * @return list of tables
	 */
	public List<Table> getLocationTables(int companyid, int locationid) throws NoResultException {
		log.debug("Getting all tables at location '" + locationid + "'");
		List<Table> tables = hostingDAO.table_restaurant_active_sel_location(companyid, locationid);
		log.debug("Got tables. Getting parties and guests");
		for (Table table : tables) {			
			try {
				log.debug("\tGetting party for table '" + table.getName() + "'");
				Party party = hostingDAO.party_active_sel_table_restaurant(companyid, table.getId());
				log.debug("\tFound party. Getting guests.");
				List<Guest> guests = hostingDAO.guest_sel_party(companyid, party.getId());
				party.setGuests(guests);
				table.setParty(party);
				log.debug("Got guests.");
			}catch(NoResultException e) {
				log.debug("This table '" + table.getName() + "' has no party");
			}
		}
		log.debug("Got all tables, parties, and guests for location '" + locationid + "'");
		
		return tables;
	}
	
	/**
	 * Gets all the open parties and guests in a given location.
	 * Guests do not have orders, for that, use waiting service
	 *  @param companyid the id of the company used to assign the connection string
	 * @param locationid the id of the location of the parties
	 * @return list of parties
	 */
	public List<Party> getOpenPartiesShallow(int companyid, int locationid) throws NoResultException {
		log.debug("Getting all parties at location '" + locationid + "'");
		List<Party> parties = hostingDAO.party_active_sel_location(companyid, locationid);
		log.debug("Got parties. Getting guests");
		
		for (Party p: parties) {
			try {
				p.setGuests(hostingDAO.guest_sel_party(companyid, p.getId()));
			} catch (NoResultException e) {
				p.setGuests(new ArrayList<Guest>(0));
			}
		}
		log.debug("Got all parties and guests for location '" + locationid + "'");
		return parties;
	}
	
	/**
	 * Creates a new party 
	 * @param party to insert
	 * @param creator the employee creating this party
	 * @return the newly created party
	 * @throws DuplicateException if a party already exists in the table
	 */
	public Party createNewParty(Party party, WMUser creator) throws DuplicateException {
		log.debug("Creating new party in the DB.");
		int partyId = hostingDAO.party_ins(creator.getCompany().getId(), party.getTableId(), party.getEmployeeId(), creator.getId());
		party.setId(partyId);
		
		log.debug("Party created. Adding guests");
		for (Guest g : party.getGuests()) {
			g.setPartyid(partyId);
			g = createNewGuest(g, creator);
		}
		
		log.debug("Successfully created party and guests '" + partyId + "'");
		return party;
	}
	
	/**
	 * Creates a new guest
	 * @param guest the guest to insert
	 * @param creator the employee creating this guest 
	 * @return the newly created guest
	 * @throws DuplicateException if a guest with the unique waitme id already exists
	 */
	public Guest createNewGuest(Guest guest, WMUser creator) throws DuplicateException {
		log.debug("Creating new guest in the DB");
		int id = hostingDAO.guest_ins(creator.getCompany().getId(), guest.getWaitmeUserid(), guest.getPartyid(), guest.getFname(), guest.getLname());
		guest.setId(id);
		log.debug("Successfully created guest '" + id + "'");
		return guest;
	}
	
	/**
	 * Reassigns a party's waiter.
	 * @param party the party to be assigned
	 * @param employee the employee to be assigned
	 * @param the user reassigning
	 */
	public Party reassignPartyWaiter(Party party, WMUser newWaiter, WMUser updater) {
		log.debug("Reassigning party '" + party.getId() + "''s waiter from '" + party.getEmployeeId() + "' to '" + newWaiter.getId() + "'");
		try {
			hostingDAO.party_upd(updater.getCompany().getId(), party.getId(), party.getTableId(), newWaiter.getId(), true, updater.getId());
		} catch (DuplicateException e) {
			log.error("Reassigning a party's employee should never throw a duplicate exception.");
		}
		party.setEmployeeId(newWaiter.getId());
		log.debug("Successfully reassigned party's waiter");
		return party;
	}
	
	/**
	 * Reassigns the party to a new table.
	 * @param party the party to be assigned
	 * @param newTable the new table to assign the party to
	 * @param updater the user reassigning
	 * @throws DuplicateException if the target table already has a party
	 */
	public Party reassignPartyTable(Party party, Table newTable, WMUser updater) throws DuplicateException {
		log.debug("Moving party '" + party.getId() + "' from table '" + party.getTableId() + "' to '" + newTable.getId() + "'");
		hostingDAO.party_upd(updater.getCompany().getId(), party.getId(), newTable.getId(), party.getEmployeeId(), true, updater.getId());
		party.setTableId(newTable.getId());
		log.debug("Successfully reassigned party's table");
		return party;
	}
	
	/**
	 * Get a list of all the clocked in employees
	 * @param location the location to search for
	 * @return list of clocked in employees
	 */
	public List<WMUser> getClockedInWMUser(int companyid, Location location) {
		log.debug("Getting all the clocked in users at '" + location.getName() + "'");
		List<WMUser> employees = new ArrayList<WMUser>(0);
		try {
			employees = hostingDAO.employee_clockedin_sel(companyid, location.getId());
		} catch (NoResultException e) {
			log.info("No clocked in employees for location '" + location.getName() + "'");
		}
		log.debug("Successfully got all (" + employees.size() + ") clocked in users at '" + location.getName() + "'");
		return employees;
	}
}