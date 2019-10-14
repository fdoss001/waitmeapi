package com.waitme.config;

import java.util.TimeZone;

import org.springframework.stereotype.Service;

/**
 * Holds all the constants for the application
 * @author Fernando Dos Santos
 * @version 1.0 2019-04-05
 * @since 1.0 2019-04-05
 */
@Service
public class Constants {
	public class Email {
		public static final String FORGOT_PASSWORD_TEMPLATE_NAME = "html/forgot-password-email";
		public static final String NEW_USER_TEMPLATE_NAME = "html/new-user-email";
		public static final String LOGO_PATH = "../../resources/graphics/waitme-wide-1024.png";
	}
	
	public class DB {
		public static final String ADMIN_USER_EXT = "DBAdmin";
		public static final String ADMIN_PASS_EXT = "1!";
		public static final String DB_STRUCTURE_SQL = "sqldump/AppStructure.sql";
		public static final String DB_STOREDPROCEDURES_SQL = "sqldump/AppStoredProcedures.sql";
	}
	
	public class Module {
		public static final String MENUS_AND_ITEMS = "Menus&Items";
		public static final String SITE_SETTINGS = "SiteSettings";
		public static final String EMPLOYEE_MANAGEMENT = "EmployeeManagement";
		public static final String POSITION_MANAGEMENT = "PositionManagement";
		public static final String LOCATION_MANAGEMENT = "LocationManagement";
		public static final String TABLE_LAYOUT = "TableLayout";
		public static final String POS_WAITER_DASHBOARD = "WaiterDashboard";
		public static final String POS_HOST_STAND = "HostStand";
	}
	
	public static final String BASE_PATH = Constants.class.getClassLoader().getResource("").getPath();
	public static final TimeZone TIME_ZONE = TimeZone.getDefault();
	
	public class POS {
		public static final int MODULE_ID = 4;
		public static final String NAME = "POS";
	}	
}
