package com.waitme.service;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import com.waitme.domain.user.WMUser;

/**
 * Http listener for whenever the http session changes
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-08
 * @since 1.0 2019-01-04
 */
@Component
public class WMHttpSessionListener implements HttpSessionListener, ApplicationContextAware {
	@Autowired
	UserService userService;
	Logger log = LoggerFactory.getLogger(WMHttpSessionListener.class);
	
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof WebApplicationContext) {
            ((WebApplicationContext) applicationContext).getServletContext().addListener(this);
        } else {
            //Either throw an exception or fail gracefully, up to you
            throw new RuntimeException("Must be inside a web application context");
        }
    }

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		//not implemented yet
	}

	//implemented to logout user when the session ends
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log.debug("Session expired.");
		WMUser wmUser = null, posWmUser = null;
		HttpSession session = se.getSession();
		try {
			wmUser = (WMUser) session.getAttribute("wmUser");
			posWmUser = (WMUser) session.getAttribute("posWmUser");
		} catch (ClassCastException e) {}//continue
		
		if (wmUser != null && wmUser.isLoggedin()) {
			userService.logoutWMUser(wmUser);
			log.debug("Logged out user '" + wmUser.getUname() + "'");
		}
		if (posWmUser != null && posWmUser.isLoggedinPos()) {
			userService.posLogoutWMUser(posWmUser);
			log.debug("POS logged out user '" + posWmUser.getUname() + "'");
		}
	}           
}