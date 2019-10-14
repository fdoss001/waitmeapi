package com.waitme.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import com.waitme.config.Constants;
import com.waitme.config.WMProperties;
import com.waitme.domain.restaurant.Company;
import com.waitme.persistence.UserDAO;
import com.waitme.utils.DBUtils;
import com.waitme.exception.NoResultException;

/**
 * Service class for connecting to DB
 * This is done as a service in the background on application start
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-08
 * @since 1.0 2019-01-04
 */
@Service
public class DBConnectionService {
	public static DriverManagerDataSource WMDATASOURCE;
	public static Map<Integer, DriverManagerDataSource> DATASOURCE_POOL;
	private static Properties spProps, dbProps;
	private static Logger log = LoggerFactory.getLogger(DBConnectionService.class);
	@Autowired
	UserDAO userDao;
	
	/**
	 * Initializes a connection to the database server.
	 * It also creates a connection pool.
	 * The main connection is to the master waitme DB which includes all global information
	 * The connection pool has a list of connections to specific company DBs.
	 * This pool is used to access data from specific companies, depending on who is making the request
	 */
	public DBConnectionService() throws NoResultException {
		spProps = new WMProperties("storedproc.properties");
		dbProps = new WMProperties("dbconnection.properties");
		
		//when initializing the service, we must get the driver name
//		Class.forName("com.mysql.cj.jdbc.Driver");
		//connect to the main application db
		WMDATASOURCE = connect(dbProps.getProperty("db"));
		
		//initialize connection pool of all company schemas
		DATASOURCE_POOL = new HashMap<Integer, DriverManagerDataSource>();
		List<Company> companies;
		try {
			companies = DBUtils.listSelectHelper(-1, spProps.getProperty("company_sel_all"), new Company());
		} catch (NoResultException e) {throw new NoResultException("No companies exist.");}
		
		for (Company comp : companies) {
			registerNewConnection(comp.getId(), comp.getDbname());
		}
	}
	
	public static void registerNewConnection(int connid, String dbname) {
		DATASOURCE_POOL.put(connid, connect(dbname));
	}
	
	/**
	 * Opens a connection to the given DB by name
	 * @param dbname the name of the DB schema on the server
	 * @return the DB connection object
	 */
	public static DriverManagerDataSource connect(String dbname) {
		log.debug("Initializing DB connection for schema '" + dbname + "'");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		String dburl = "jdbc:mysql://" + dbProps.getProperty("host") + ":" + dbProps.getProperty("port") + "/" + dbname + "?noAccessToProcedureBodies=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=" + TimeZone.getDefault().getID();
		log.debug("The connection string is:\n\t" + dburl);
		dataSource.setUrl(dburl);		
		dataSource.setUsername(dbname.substring(0,1).toUpperCase() + dbname.substring(1) + Constants.DB.ADMIN_USER_EXT);
		dataSource.setPassword(dbname.substring(0,1).toUpperCase() + dbname.substring(1) + Constants.DB.ADMIN_PASS_EXT);
		
		return dataSource;
	}
}
