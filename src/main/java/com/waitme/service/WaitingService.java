package com.waitme.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.waitme.domain.item.Item;
import com.waitme.domain.item.ItemInOrder;
import com.waitme.domain.item.ItemOption;
import com.waitme.domain.item.MainCategory;
import com.waitme.domain.item.Meal;
import com.waitme.domain.item.Menu;
import com.waitme.domain.item.OptionOnOrderItem;
import com.waitme.domain.item.Order;
import com.waitme.domain.item.SubCategory;
import com.waitme.domain.restaurant.EPayMethod;
import com.waitme.domain.restaurant.Guest;
import com.waitme.domain.restaurant.Party;
import com.waitme.domain.user.WMUser;
import com.waitme.persistence.HostingDAO;
import com.waitme.persistence.WaitingDAO;
import com.waitme.utils.RestaurantUtils;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoResultException;
import com.waitme.exception.WaitingException;

/**
 * Service class for waiting related functions
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-14
 * @since 1.0 2019-02-08
 */
@Service
public class WaitingService {
	@Autowired
	WaitingDAO waitingDAO;
	@Autowired
	HostingDAO hostingDAO;
	@Autowired
	RestaurantManagementService restaurantManagementService;
	
	Logger log = LoggerFactory.getLogger(WaitingService.class);
	
	public WaitingService() {}

	public WaitingService(WaitingDAO wd) {
		waitingDAO = wd;
	}
	
	/**
	 * Gets a guest from the DB with all the info including orders, items, and options
	 * @param companyid the company this is happening on
	 * @param guestid the id of the guest to get
	 * @return the complete guest object
	 * @throws NoResultException if the guest does not exist
	 */
	public Guest getGuest(int companyid, int guestid) throws NoResultException {
		Guest guest = waitingDAO.guest_sel(companyid, guestid);
		
		List<Order> orders = new ArrayList<Order>(0);
		try {
			orders = getOrdersForGuest(companyid, guestid);
		} catch (NoResultException e) {
			log.debug("This guest '" + guest.getFname() + "' has no orders. Continuing...");
		}
		guest.setOrders(orders);
		
		return guest;
	}
	
	/**
	 * Gets a party with the guests from the DB.
	 * This is a deep select. This means that the guests have order information including items and options.
	 * @param companyid the company this is happening on
	 * @param partyid the id of the party to get
	 * @return the party object with all the info
	 * @throws NoResultException if the party does not exist
	 */
	public Party getPartyDeep(int companyid, int partyid) throws NoResultException {
		Party party = getPartyShallow(companyid, partyid);
		
		for (Guest guest : party.getGuests()) {
			List<Order> orders = new ArrayList<Order>(0);
			try {
				orders = getOrdersForGuest(companyid, guest.getId());
			} catch (NoResultException e) {
				log.debug("This guest '" + guest.getFname() + "' has no orders. Continuing...");
			}
			guest.setOrders(orders);
		}
		
		return party;
	}
	
	/**
	 * Gets a party with the guests from the DB.
	 * The guests do not include order information.
	 * @param companyid the company this is happening on
	 * @param partyid the id of the party to get
	 * @return the party object with guests
	 * @throws NoResultException if the party does not exist
	 */
	public Party getPartyShallow(int companyid, int partyid) throws NoResultException {
		Party party = waitingDAO.party_sel(companyid, partyid);
		
		List<Guest> guests = new ArrayList<Guest>(0);
		try {
			guests = waitingDAO.guest_sel_party(companyid, partyid);
		} catch (NoResultException e) {
			log.debug("This party '" + party.getId() + "' has no guests. Continuing...");
		}
		party.setGuests(guests);
		
		return party;
	}
	
	/**
	 * Gets all the orders that a guest currently has open
	 * @param companyid the company this is happening on
	 * @param guestid the id of the guest to get ther orders for
	 * @return the list of orders for the guest
	 * @throws NoResultException if the guest has no orders
	 */
	public List<Order> getOrdersForGuest(int companyid, int guestid) throws NoResultException {
		List<Order> orders = waitingDAO.order_open_sel_guest(companyid, guestid);
		for (Order order : orders) {
			List<ItemInOrder> items = new ArrayList<ItemInOrder>(0);
			try {
				items = waitingDAO.order_item_item_sel(companyid, order.getId());
			} catch (NoResultException e) {
				log.debug("This order '" + order.getId() + "' has no items. Continuing...");
			}
			for (ItemInOrder item : items) {
				List<OptionOnOrderItem> options = new ArrayList<OptionOnOrderItem>(0);
				try {
					options = waitingDAO.order_item_option_item_option_sel(companyid, item.getOrderItemId());
				} catch (NoResultException e) {
					log.debug("This order item '" + item.getOrderItemId() + "' has no options. Continuing...");
				}
				item.setOptions(options);
			}
			order.setItems(items);
		}
		return orders;
	}
	
	/**
	 * Opens up a new order in the DB.
	 * This does not insert any items or options
	 * @param order the order to open
	 * @param guest the guest to open it under
	 * @param creator the employee creating the order
	 * @return the new order with the id
	 */
	public Order createNewOrder(Order order, Guest guest, WMUser creator) {
		int orderid = waitingDAO.order_open_ins(creator.getCompany().getId(), guest.getId(), creator.getId());
		order.setId(orderid);
		
//		if (order.getItems() != null) {
//			for (ItemInOrder item : order.getItems()) {
//				int orderItemId = waitingDAO.order_item_ins(creator.getCompany().getId(), orderid, item.getId());
//				item.setOrderItemId(orderItemId);
//		
//				if (item.getOptions() != null) {
//					for (OptionOnOrderItem option : item.getOptions()) {
//						int orderOptionId = waitingDAO.order_item_option_ins(creator.getCompany().getId(), orderItemId, option.getId());
//						option.setOrderOptionId(orderOptionId);
//					}
//				}
//			}
//		}
		
		return order;
	}
	
	/**
	 * Adds an item to an order in the DB
	 * This does not insert the items options.
	 * @param companyid the id of the company this is happening on
	 * @param order the order to add the item to
	 * @param item the item to add to the order
	 * @return the updated order
	 */
	public Order addItemToOrder(int companyid, Order order, ItemInOrder item) {
		int orderItemId = waitingDAO.order_item_ins(companyid, order.getId(), item.getId());
		item.setOrderItemId(orderItemId);
		
//		if (item.getOptions() != null) {
//			for (OptionOnOrderItem option : item.getOptions()) {
//				int orderOptionId = waitingDAO.order_item_option_ins(companyid, orderItemId, option.getId());
//				option.setOrderOptionId(orderOptionId);
//			}
//		}
		order.addItem(item);
		return order;
	}
	
	/**
	 * Adds an option to an item in an order in the DB
	 * @param companyid the id of the company this is happening on
	 * @param item the item to add the option to
	 * @param option the option to add to the item
	 * @return the updated item
	 */
	public ItemInOrder addOptionToOrderItem(int companyid, ItemInOrder item, OptionOnOrderItem option) {
		int orderOptionId = waitingDAO.order_item_option_ins(companyid, item.getOrderItemId(), option.getId());
		option.setOrderOptionId(orderOptionId);
		item.addOption(option);
		return item;
	}

	/**
	 * Closes an order in the DB
	 * @param order the order to close
	 * @throws NoResultException 
	 */
	public void closeOrder(WMUser closer, Guest guest, int orderid, boolean paid, EPayMethod payMethod, BigDecimal payAmount, boolean voided, int voidAuthUserId, String voidReason) throws NoResultException {
		Order order = null;
		int i = 0;
		for (Order o : guest.getOrders()) {
			if (o.getId() == orderid) {
				order = o;
				guest.getOrders().remove(i);
				break;
			}
			i++;
		}
		if (order == null) {
			throw new NoResultException("The order '" + orderid + "' does not exist for guest '" + guest.getFname() + "'");
		}
		
		//closing order
		waitingDAO.order_closed_ins(closer.getCompany().getId(), order.getId(), paid, payMethod.toString(), payAmount, closer.getId(), voided, voidAuthUserId, voidReason);
	}
	
	/**
	 * Closes a party in the DB
	 * @param party the party to close
	 * @throws WaitingException if the party still has open orders
	 */
	public void closeParty(Party party, WMUser closer) throws WaitingException {
		for (Guest guest : party.getGuests()) {
			if (guest.getOrders() != null && guest.getOrders().size() > 0) {
				throw new WaitingException("This party still has open orders.");
			}
		}
		
		try {
			hostingDAO.party_upd(closer.getCompany().getId(), party.getId(), party.getTableId(), party.getEmployeeId(), false, closer.getId());
		} catch (DuplicateException e) {
			log.error("Should never throw duplicate because keys aren't being updated");
		}
	}
	
	/** 
	 * Gets the parties currently assigned to the given user
	 * The parties are shallow, meaning they contain up to guest info, but no more.
	 * @param waiter waiter to check for
	 * @return a list of parties assigned to the waiter
	 * @throws NoResultException if the waiter has no parties assigned
	 */
	public List<Party> getPartiesForWaiterShallow(WMUser waiter) throws NoResultException {
		List<Party> parties = waitingDAO.party_active_sel_employee(waiter.getCompany().getId(), waiter.getId());

		int i = 0;
		for (Party party : parties) {
			//this will not throw no result because the party exists
			party = getPartyShallow(waiter.getCompany().getId(), party.getId());
			parties.set(i, party);
			i++;
		}
		
		return parties;
	}
	
	/** 
	 * Gets the parties currently assigned to the given user
	 * @param waiter waiter to check for
	 * @return a list of parties assigned to the waiter
	 * @throws NoResultException if the waiter has no parties assigned
	 */
	public List<Party> getPartiesForWaiterDeep(WMUser waiter) throws NoResultException {
		List<Party> parties = waitingDAO.party_active_sel_employee(waiter.getCompany().getId(), waiter.getId());
		
		int i = 0;
		for (Party party : parties) {
			//this will not throw no result because the party exists
			party = getPartyDeep(waiter.getCompany().getId(), party.getId());
			parties.set(i, party);
			i++;
		}
		
		return parties;
	}
	
	/**
	 * Gets all active, full menus with all sub lists for the given location
	 * @param locationid the id of the location to get the menus for
	 * @return the list of menu objects
	 * @throws NoResultException if the location has no menus
	 */
	@SuppressWarnings("unchecked") //the casts to the correct list type are not checked here as it is not needed
	public List<Menu> getAllActiveMenus(int companyid, int locationid) throws NoResultException {
		List<Menu> menus = restaurantManagementService.getAllMenusMinimal(companyid, locationid);
		menus = (List<Menu>) RestaurantUtils.removeInactive(menus);
		return menus;
	}
	
	/**
	 * Get all active main categories from the DB
	 * @return the list of all item objects
	 */
	@SuppressWarnings("unchecked") //the casts to the correct list type are not checked here as it is not needed
	public List<MainCategory> getAllMainCategories(int companyid) throws NoResultException {
		List<MainCategory> mainCategories = restaurantManagementService.getAllMainCategoriesMinimal(companyid);
		mainCategories = (List<MainCategory>) RestaurantUtils.removeInactive(mainCategories);
		return mainCategories;
	}
	
	/**
	 * Get all active sub categories from the DB
	 * @return the list of all item objects
	 */
	@SuppressWarnings("unchecked") //the casts to the correct list type are not checked here as it is not needed
	public List<SubCategory> getAllActiveSubCategories(int companyid) throws NoResultException {
		List<SubCategory> subCategories = restaurantManagementService.getAllSubCategoriesMinimal(companyid);
		subCategories = (List<SubCategory>) RestaurantUtils.removeInactive(subCategories);
		return subCategories;
	}
	
	/**
	 * Get all active meals from the DB
	 * @return the list of all item objects
	 */
	@SuppressWarnings("unchecked") //the casts to the correct list type are not checked here as it is not needed
	public List<Meal> getAllActiveMeals(int companyid) throws NoResultException {
		List<Meal> meals = restaurantManagementService.getAllMealsMinimal(companyid);
		meals = (List<Meal>) RestaurantUtils.removeInactive(meals);
		return meals;
	}
	
	/**
	 * Get all active items from the DB
	 * @return the list of all item objects
	 */
	@SuppressWarnings("unchecked") //the casts to the correct list type are not checked here as it is not needed
	public List<Item> getAllActiveItems(int companyid) throws NoResultException {
		List<Item> items = restaurantManagementService.getAllItemsMinimal(companyid);
		items = (List<Item>) RestaurantUtils.removeInactive(items);
		return items;
	}
	
	/**
	 * Get all active options from the DB
	 * @return the list of all option objects
	 */
	@SuppressWarnings("unchecked") //the casts to the correct list type are not checked here as it is not needed
	public List<ItemOption> getAllActiveOptions(int companyid) throws NoResultException {
		List<ItemOption> options = restaurantManagementService.getAllItemOptionsMinimal(companyid);
		options = (List<ItemOption>) RestaurantUtils.removeInactive(options);
		return options;
	}
}
