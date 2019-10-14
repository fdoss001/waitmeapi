package com.waitme.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.waitme.config.Constants;
import com.waitme.exception.DeleteException;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoResultException;
import com.waitme.service.DBConnectionService;

/**
 * Utility class for reusability of DB commands
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-12
 * @since 1.0 2019-01-04
 */
public class DBUtils {
	private static Logger log = LoggerFactory.getLogger(DBUtils.class);
	
	/**
	 * Gets the jdbc template for the given data source
	 * @param companyid the connection id for the company's database
	 * @return the jdbctemplate to query with
	 */
	public static JdbcTemplate getTemplateForCompany(int companyid) {
		if (companyid < 0) {
			return new JdbcTemplate(DBConnectionService.WMDATASOURCE);
		} else {
			return new JdbcTemplate(DBConnectionService.DATASOURCE_POOL.get(companyid));
		}
	}
	
	/**
	 * A wrapper for selecting a list of objects from a stored procedure.
	 * The objects must implement row mapper.
	 * @param <T> The type of the object in the list to be returned
	 * @param companyid the company id to connect to the corresponding db
	 * @param sp the stored procedure to run
	 * @param type the object class (must implement rowmapper). ex. new Object()
	 * @param args the arguments for the stored procedure
	 * @return the list of objects built from the data
	 * @throws NoResultException if no results were returned
	 */
	public static <T> List<T> listSelectHelper(int companyid, String sp, RowMapper<T> type, Object... args) throws NoResultException {
		try {
			List<T> list = getTemplateForCompany(companyid).query(sp, type, args);
			
			if (list == null || list.isEmpty()) {
				throw new NoResultException("This stored procedure '" + sp + "' returned an empty list for the argument(s) '" + args + "'.");
			}
			
			return list;
		} catch (EmptyResultDataAccessException | UncategorizedSQLException e) {
			if (e instanceof UncategorizedSQLException) {
				log.error(e.getMessage());
			}
			throw new NoResultException("This stored procedure '" + sp + "' returned an empty list for the argument(s) '" + args + "'.");
		}
	}
	
	/**
	 * A wrapper for selecting a single object from a stored procedure.
	 * The object must implement row mapper
	 * @param <T> The type of the object to be returned
	 * @param companyid the company id to connect to the corresponding db
	 * @param sp the stored procedure to run
	 * @param type the object class (must implement rowmapper). ex. new Object()
	 * @param args the arguments for the stored procedure
	 * @return the object built from the data
	 * @throws NoResultException if no object was found
	 */
	public static <T> T objectSelectHelper(int companyid, String sp, RowMapper<T> type, Object... args) throws NoResultException {
		try {	
			T object = getTemplateForCompany(companyid).queryForObject(sp, type, args);
			
			if (object == null) {
				throw new NoResultException("This stored procedure '" + sp + "' did not return an object for '" + args + "'.");
			}
			
			return object;
		} catch (EmptyResultDataAccessException e) {
			throw new NoResultException("This stored procedure '" + sp + "' did not return an object for '" + args + "'.");
		}
	}
	
	/**
	 * A wrapper for selecting a single primitive data type from a stored procedure,
	 * such as an integer or string.
	 * @param <T> The primitive type of the object to be returned
	 * @param companyid the company id to connect to the corresponding db
	 * @param sp the stored procedure to run
	 * @param type the primitive object wrapper class. ex Integer.class
	 * @param args the arguments for the stored procedure
	 * @return the single result as a primitive type wrapper
	 * @throws NoResultException if no result was returned
	 */
	public static <T> T primitiveSelectHelper(int companyid, String sp, Class<T> type, Object... args) throws NoResultException {
		try {
			T object = getTemplateForCompany(companyid).queryForObject(sp, type, args);
			
			if (object == null) {
				throw new NoResultException("This stored procedure '" + sp + "' did not return a primitive for '" + args + "'.");
			}
			
			return object;
		} catch (EmptyResultDataAccessException e) {
			throw new NoResultException("This stored procedure '" + sp + "' did not return a primitive for '" + args + "'.");
		}
	}
	
	/**
	 * A wrapper for inserting an entry into the db and returning the id
	 * @param companyid the company id to connect to the corresponding db
	 * @param sp the stored procedure to run
	 * @param args the arguments for the stored procedure
	 * @return the id of the last inserted item
	 * @throws DuplicateException if it could not insert due to duplicate key constraints
	 */
	public static int insertHelper(int companyid, String sp, Object... args) throws DuplicateException {
		try {
			int id = getTemplateForCompany(companyid).queryForObject(sp, Integer.class, args);
			return id;
		} catch (DuplicateKeyException e) {
			throw new DuplicateException("Duplicate primary key(s). Cannot insert entry");
		}
	}
	
	/**
	 * A wrapper for updating entries in the db
	 * @param companyid the company id to connect to the corresponding db
	 * @param sp the stored procedure to run
	 * @param args the arguments for the stored procedure
	 * @throws DuplicateException if it could not update due to duplicate key constraints
	 */
	public static void updateHelper(int companyid, String sp, Object... args) throws DuplicateException {
		try {
			getTemplateForCompany(companyid).update(sp, args);
		} catch (DuplicateKeyException e) {
			throw new DuplicateException("Duplicate unique value. Cannot update entry");
		}
	}	
	
	/**
	 * A wrapper for deleting entries in the db
	 * @param companyid the company id to connect to the corresponding db
	 * @param sp the stored procedure to run
	 * @param args the arguments for the stored procedure
	 * @throws DeleteException if it could not delete due to any user placed constraints
	 */
	public static void deleteHelper(int companyid, String sp, Object... args) throws DeleteException {
		try {
			getTemplateForCompany(companyid).update(sp, args);
		} catch (UncategorizedSQLException e) {
			throw new DeleteException(e.getSQLException().getMessage());
		}
	}
	
	/**
	 * A wrapper for simply running a stored procedure.
	 * It will not return anything nor throw any checked exceptions.
	 * Only use this if you want that intended result.
	 * @param companyid the company id to connect to the corresponding db
	 * @param sp the stored procedure to run
	 * @param args the arguments for the stored procedure
	 */
	public static void runSPHelper(int companyid, String sp, Object... args) {
		getTemplateForCompany(companyid).update(sp, args);
	}
	
//	public static void genericSPHelper(int companyid, String sp, Class<Exception> exception, Object... args) throws Exception {
//		try {
//			getTemplateForCompany(companyid).update(sp, args);
//		} catch (UncategorizedSQLException e) {
//			throw exception.getDeclaredConstructor(String.class).newInstance(e.getMessage());
//		}
//	}	
	
	/**
	 * Starts a command line process to run the sql file
	 * Must be run from a unix environment to work
	 * @param filePath path to the file of the .sql script to run
	 */
	public static void sqlFileExec(String dbname, String filePath) throws IOException, InterruptedException {
		String user = dbname.substring(0,1).toUpperCase() + dbname.substring(1) + Constants.DB.ADMIN_USER_EXT;
		String pass = dbname.substring(0,1).toUpperCase() + dbname.substring(1) + Constants.DB.ADMIN_PASS_EXT;
		String command = "/usr/bin/mysql -u " + user + " -p" + pass + " " + dbname + " < " + filePath;
		
		List<String> params = new ArrayList<String>(3);
		params.add("sh");
		params.add("-c");
		params.add(command);
		ProcessBuilder pb = new ProcessBuilder(params);
		
		log.debug("Running sql command.");
		Process proc = pb.start();
		proc.waitFor();
		log.debug("Command complete");
	}
}
