package com.waitme.domain.user;

import com.waitme.domain.WMDomainObject;

/**
 * Class to represent a street address
 * @author Fernando Dos Santos
 * @version 1.0 2019-04-04
 * @since 1.0 2019-04-04
 */
public class PostalAddress extends WMDomainObject {
	private String address1, address2, city, state, zip;

	public PostalAddress() {}
	
	/**
	 * Builds an address object out of the given string.
	 * It must be formed in separate sections delimited by a ;
	 * @param address the string to format
	 */
	public PostalAddress(String address) {
		if (address != null && !address.isEmpty()) {
			String[] params = address.split(";");
			this.address1 = params[0];
			this.address2 = params[1];
			this.city = params[2];
			this.state = params[3];
			this.zip = params[4];
		}
	}
	
	public PostalAddress(String address1, String address2, String city, String state, String zip) {
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	
	@Override
	public String toString() {
		//the extra 8 at the end is to account for the commas and spaces
		StringBuilder sb = new StringBuilder(address1.length() + address2.length() + city.length() + state.length() + zip.length() + 8);
		if (!address1.isEmpty())
			sb.append(address1 + ", ");
		if (!address2.isEmpty())
			sb.append(address2 + ", ");
		if (!city.isEmpty())
			sb.append(city + ", ");
		if (!state.isEmpty())
			sb.append(state + ", ");
		if (!zip.isEmpty())
			sb.append(zip);
		return sb.toString();
	}
	
	public String serialize() {
		if (address1 == null || address1.isEmpty())
			return null;
		return address1 + ';' + address2 + ';' + city + ';' + state + ';' + zip;
	}
}
