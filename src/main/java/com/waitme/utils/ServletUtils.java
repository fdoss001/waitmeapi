package com.waitme.utils;

import java.util.NoSuchElementException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.waitme.domain.user.WMUser;

/**
 * Utility class for reusabilty in servlet functions
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-08
 * @since 1.0 2019-01-29
 */
public class ServletUtils {	
	/**
	 * This method is to be called in the first line of every controller's init (GET) function.
	 * It prints the controller name to the console, along with the sessionid and username (if available)
	 * It then processes a redirect message if there is one
	 * @param session the HTTP session
	 * @param model the model from the controller
	 * @param caller the class that is calling this method
	 */
	public static void servletInit(HttpServletRequest request, Model model, Class<?> caller) {
		HttpSession session = request.getSession();
		WMLogger log = new WMLogger(caller);
		try {
			WMUser wmUser = (WMUser) session.getAttribute("wmUser");
			log.info(caller.getName() + ": " + wmUser.getUname() + " -- " + session.getId());
		} catch (ClassCastException | NullPointerException e) {
			log.info(caller.getName() + ": "  + request.getRemoteAddr() + " -- " + session.getId());
		}
		
		processRedirectMessage(session, model);
	}
	
	/**
	 * Finds the given cookie by name in the request object and deletes its content and age
	 * @param name the name of the cookie to delete
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(name)) {
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
				break;
			}
		}
	}
	
	/**
	 * Returns the given cookie by name
	 * @param name the name of the cookie to get
	 * @return the cookie object
	 * @throws NoSuchElementException if the cookie is not found
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) throws NoSuchElementException {
		for (Cookie cookie : request.getCookies()) {
    		if (cookie.getName().equals(name)) {
    			return cookie;
    		}
    	}
		throw new NoSuchElementException("The cookie with name '" + name + "' was not found");
	}
	
	/**
	 * Deletes a global redirect message from the session and adds it to the model.
	 * This is needed because addredirectattributes isn't always available
	 * @param session the session with the message
	 * @param model the model to add it to
	 */
	public static void processRedirectMessage(HttpSession session, Model model) {
		String message = (String) session.getAttribute("redirectMessage");
		if (message != null && !message.isEmpty()) {
			model.addAttribute("redirectMessage", message);
			session.removeAttribute("redirectMessage");
		}
	}
}
