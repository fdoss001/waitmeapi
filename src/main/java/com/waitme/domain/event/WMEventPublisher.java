package com.waitme.domain.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.waitme.domain.user.WMUser;

/**
 * NOT BEING USED
 * Class to represent a custom waitme event publisher
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-08
 * @since 1.0 2019-02-08
 */
@Component
public class WMEventPublisher {
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	public void publishWMUserLoginEvent(final WMUser wmUser, final String message) {
		WMUserLoginEvent event = new WMUserLoginEvent(this, wmUser, message);
		applicationEventPublisher.publishEvent(event);
	}
}
