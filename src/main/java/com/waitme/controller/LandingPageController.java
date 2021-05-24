package com.waitme.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.waitme.config.Constants;
import com.waitme.utils.WMLogger;

@Controller
public class LandingPageController {
	@Value("${application.name}")
	String appName;
	
	@Value("${application.version}")
	String version;
	
	WMLogger log = new WMLogger(LandingPageController.class);
	
	@GetMapping("/")
	public String landingPage(Model model) {
		log.debug("Base Path = " + Constants.BASE_PATH);
		model.addAttribute("appName", appName);
		model.addAttribute("version", version);
		return "landing";
	}
	
//	@GetMapping("/index")
//	public String index(HttpServletRequest request) {
////		String resource = request.getRequestURI().substring(request.getContextPath().length());
////		File dir = new File(Constants.BASE_PATH + "templates");
////		File[] files = dir.listFiles();
////		for(File f : files) {
////			if (resource.contains(f.getName()))
////				return f.getName();
////		}
//		return "index";
//	}
}
