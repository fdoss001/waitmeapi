package com.waitme.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.waitme.domain.restaurant.IActivatable;

/**
 * Utility class for helpers regarding restaurant functions
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-28
 * @since 1.0 2019-02-28
 */
public class RestaurantUtils {
	private static Logger log = LoggerFactory.getLogger(RestaurantUtils.class);
	/**
	 * Removes inactive objects from a list of activatable objects
	 * @param fullList the list of all objects
	 * @return a list without the inactive objects
	 */
	public static List<? extends IActivatable> removeInactive(List<? extends IActivatable> fullList) {
		log.debug("Removing all inactive elements from '" + fullList.getClass().getName() + "'");
		ArrayList<IActivatable> activeList = new ArrayList<IActivatable>(fullList.size());
		for (IActivatable a : fullList) {
			if (a.isActive()) {
				activeList.add(a);
			}
		}
		activeList.trimToSize();
		log.debug("Successfully removed all inactive elements.");
		return activeList;
	}
}
