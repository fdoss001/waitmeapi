package com.waitme.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.waitme.config.WMProperties;
import com.waitme.domain.item.ItemInOrder;
import com.waitme.domain.item.OptionOnOrderItem;
import com.waitme.domain.item.Order;
import com.waitme.domain.restaurant.Guest;
import com.waitme.domain.restaurant.Party;
import com.waitme.exception.NoResultException;
import com.waitme.utils.DBUtils;

/**
 * Implementation persistence class for interacting with tables
 * related to waiting services in DB
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-14
 * @since 1.0 2019-02-08
 */
@Component
public class WaitingDAO {
	private Properties spProps;
	
	public WaitingDAO() {
		spProps = new WMProperties("storedproc.properties");		
	}
	
	public Guest guest_sel(int companyid, int guestid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("guest_sel"), new Guest(), guestid);
	}
	
	public List<Party> party_active_sel_employee(int companyid, int employeeid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("party_active_sel_employee"), new Party(), employeeid);
	}
	
	public Party party_sel(int companyid, int partyid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("party_sel"), new Party(), partyid);
	}
	
	public List<Guest> guest_sel_party(int companyid, int partyid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("guest_sel_party"), new Guest(), partyid);
	}
	
	public int order_open_ins(int companyid, int guestid, int updaterid) {
		return DBUtils.getTemplateForCompany(companyid).queryForObject(spProps.getProperty("order_open_ins"), Integer.class, guestid, updaterid);
	}
	
	public int order_item_ins(int companyid, int orderid, int itemid) {
		return DBUtils.getTemplateForCompany(companyid).queryForObject(spProps.getProperty("order_item_ins"), Integer.class, orderid, itemid);
	}
	
	public int order_item_option_ins(int companyid, int orderitemid, int optionid) {
		return DBUtils.getTemplateForCompany(companyid).queryForObject(spProps.getProperty("order_item_option_ins"), Integer.class, orderitemid, optionid);
	}
	
	public List<Order> order_open_sel_guest(int companyid, int guestid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("order_open_sel_guest"), new Order(), guestid);
	}
	
	public List<ItemInOrder> order_item_item_sel(int companyid, int orderid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("order_item_item_sel"), new ItemInOrder(), orderid);
	}
	
	public List<OptionOnOrderItem> order_item_option_item_option_sel(int companyid, int itemid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("order_item_option_item_option_sel"), new OptionOnOrderItem(), itemid);
	}
	
	public void order_closed_ins(int companyid, int orderid, boolean paid, String payMethod, BigDecimal payAmount, int closerid, boolean voided, int voidAuthUserId, String voidReason) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("order_closed_ins"), orderid, paid, payMethod, payAmount, closerid, voided, voidAuthUserId, voidReason);
	}
}
