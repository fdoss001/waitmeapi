package com.waitme.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.waitme.config.Constants;

@Controller
public class LandingPageController {
	@Value("${application.name}")
	String appName;
	
	@Value("${application.version}")
	String version;
	
	Logger log = LoggerFactory.getLogger(LandingPageController.class);
	
	@GetMapping("/")
	public String landingPage(Model model) {
		log.debug("Base Path = " + Constants.BASE_PATH);
		model.addAttribute("appName", appName);
		model.addAttribute("version", version);
		return "landing";
	}
}
