package com.waitme.service;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.waitme.config.Constants;
import com.waitme.config.WMProperties;
import com.waitme.domain.restaurant.Company;
import com.waitme.domain.user.WMUser;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoResultException;
import com.waitme.persistence.CompanyDAO;
import com.waitme.utils.DBUtils;
import com.waitme.utils.WMLogger;

/**
 * Service class for company related functions
 * @author Fernando Dos Santos
 * @version 1.0 2019-07-22
 * @since 1.0 2019-07-22
 */
@Service
public class CompanyService {
	WMLogger log = new WMLogger(CompanyService.class);
	private Properties messageProps;
	
	@Autowired
	CompanyDAO companyDAO;
	
	public CompanyService() {
		messageProps = new WMProperties("messages.properties");
	}

	/**
	 * Registers a new company with WaitMe by adding their info to the waitme DB
	 * @param company the new company to register
	 * @param creatorUname the creator of the company
	 * @return the new company with correct info
	 * @throws DuplicateException if the company name already exists
	 */
	public Company registerNewCompany(Company company, String creatorUname) throws DuplicateException {
		//Insert the company into the DB. Settings inserted by default
		log.debug("Creating a new company with name '" + company.getName() + "'");
		
		try {			
			int id = companyDAO.company_ins(company.getName(),creatorUname);
			//get the company from the DB in order to get all the details
			company = getCompany(id);
		} catch (DuplicateException e) {
			log.error("Duplicate name '" + company.getName() + "'. Cannot create company.");
			throw new DuplicateException(messageProps.getProperty("error.duplicate_companyname"));
		} catch (NoResultException e) {
			log.error("Should not happen because company '" + company.getName() + "' was just inserted so it must exist.");
		}
		
		log.debug("Successfully registered company with WaitMe. The id is '" + company.getId() + "'");
		return company;
	}
	
	/**
	 * Creates a new company's database schema, and opens the connection
	 * @param company the company to create the schema for
	 */
	public void createCompanyDatabase(Company company) throws IOException {
		log.debug("Creating database '" + company.getDbname() + "'");
		//create the database via the stored procedure
		companyDAO.create_new_schema(company.getDbname());
		
		//open a connection to the new schema
		log.debug("Database and admin created successfully. Connecting to new DB.");
		DBConnectionService.registerNewConnection(company.getId(), company.getDbname());
		log.debug("Connected successfully");
	}
	
	/**
	 * Creates all the company's required tables in the DB
	 * @param dbname the name of the company's database
	 */
	public void createCompanyTables(String dbname) throws IOException, InterruptedException {
		log.debug("Creating tables in DB.");
		String sqlFile = Constants.BASE_PATH + Constants.DB.DB_STRUCTURE_SQL;
		DBUtils.sqlFileExec(dbname, sqlFile);
		log.debug("Tables created successfully");
	}
	
	/**
	 * Creates all the company's required stored procedures in the DB
	 * @param dbname
	 */
	public void createCompanySPs(String dbname) throws IOException, InterruptedException {
		log.debug("Creating SPs for '" + dbname + "'");
		String sqlFile = Constants.BASE_PATH + Constants.DB.DB_STOREDPROCEDURES_SQL;
		DBUtils.sqlFileExec(dbname, sqlFile);
		log.debug("SPs created successfully");
	}
	
	/**
	 * Gets a company object from the DB
	 * @param companyid the id of the company to get
	 * @return the company object
	 * @throws NoResultException if the company does not exist
	 */
	public Company getCompany(int companyid) throws NoResultException {
		log.debug("Getting company with id '" + companyid + "'");		
		Company company = companyDAO.company_sel(companyid);
		log.debug("Successfully got the company.");		
		return company;
	}
	
	/**
	 * Updates a company's settings in the DB.
	 * The updated settings must be in the passed company object 
	 * @param company the company to update the settings for
	 * @param updater the user updating this company
	 */
	public void updateCompanySettings(Company company, WMUser updater) {
		log.debug("Updating company '" + company.getName() + "'");
		companyDAO.company_settings_upd(company.getId(), company.getSettings().getLogoPath(), company.getSettings().getThemePath(), updater.getUname());
		log.debug("Successfully updated company.");
	}
	
	public void validateDuplicateUser(String uname) throws DuplicateException {
		int count = 0;
		try {
			count = companyDAO.company_user_sel_cnt(uname);
		} catch (NoResultException e) {log.debug("Will never happen because select count always returns");}
		if (count > 0) {
			throw new DuplicateException(messageProps.getProperty("error.duplicate_username"));
		}
	}
}
