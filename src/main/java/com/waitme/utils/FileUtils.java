package com.waitme.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.waitme.config.Constants;
import com.waitme.config.WMProperties;
import com.waitme.domain.restaurant.Company;
import com.waitme.domain.user.WMUser;

/**
 * Utility class for file operations
 * @author Fernando Dos Santos
 * @version 1.0 2019-04-02
 * @since 1.0 2019-04-02
 */
public class FileUtils {

	private static Logger log = LoggerFactory.getLogger(FileUtils.class);
	private static Properties messageProps = new WMProperties("messages.properties");
	
	/**
	 * Copies a file and replaces the target
	 * @param copiedPath the file to copy
	 * @param replacePath the file to replace
	 * @param the file context
	 */
	public static void copyReplaceFile(String copiedPath, String replacePath) throws IOException {
		log.debug("Copying file '" + copiedPath + "' and replacing '" + replacePath + "'");
		File currentFile = new File(replacePath);
		if (currentFile.exists())
			currentFile.delete();
		
		InputStream in = null;
		OutputStream out = null;
		try {
			 in = new BufferedInputStream(new FileInputStream(copiedPath));
			 out = new BufferedOutputStream(new FileOutputStream(replacePath));
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) > 0) {
				out.write(buffer, 0, read);
				out.flush();
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			in.close();
			out.close();
			log.debug("Successfully copied and replaced file.");
		}
	}
	
	/**
	 * Uploads an icon for the user in their icon path
	 * @param wmUser the user to upload the icon for. This determines their file path
	 * @param imageBytes the bytes of the image file
	 * @param ext the file extension of the image such as jpg or png
	 * @return the icon path with the file included
	 */
	public static String uploadUserIcon(WMUser wmUser, byte[] imageBytes, String ext) {
		Properties props = new WMProperties("user.properties");
		String usrPath = Constants.BASE_PATH + props.getProperty("settings.path") + File.separator + wmUser.getUname();

		log.debug("Uploading icon '" + props.getProperty("settings.icon") + "." + ext + "' for user '" + wmUser.getUname() + "' to '" + usrPath + "'");
		
		//delete the old icon file if it exists
		if (wmUser.getIconPath() != null) {
			log.debug("The file already exists. Deleting");
			String[] oldFileName;
			try {
				oldFileName = wmUser.getIconPath().split(File.separator);
			} catch(PatternSyntaxException e) {
				log.warn("Error due to windows upload. Resolving regex");
				oldFileName = wmUser.getIconPath().split(File.separator + File.separator);
			}
			File oldFile = new File(usrPath + File.separator + oldFileName[oldFileName.length - 1]);
			oldFile.delete();
		}
		
		//create the file
		String newIconFilePath = usrPath + File.separator + props.getProperty("settings.icon") + "." + ext;
		try {
			createFile(imageBytes, newIconFilePath);
			wmUser.setIconPath(props.getProperty("settings.basepath") + File.separator + wmUser.getUname() + File.separator + props.getProperty("settings.icon") + "." + ext);
		} catch (IOException e) {
			return e.getMessage();
		}
		log.debug("Successfully uploaded icon. Its URL is at '" + wmUser.getIconPath() + "'");
		return wmUser.getIconPath();
	}
	
	/**
	 * Uploads a logo for the company in their logo path
	 * @param company the company to upload the logo for. This determines their file path
	 * @param imageBytes the bytes of the image file
	 * @param ext the file extension of the image such as jpg or png
	 * @return the logo path with the file included
	 * @throws IOException if there was an error during upload
	 */
	public static String uploadCompanyLogo(Company company, byte[] imageBytes, String ext) throws IOException {
		Properties props = new WMProperties("user.properties");
		String compPath = Constants.BASE_PATH + props.getProperty("settings.company.path") + File.separator + company.getName().toLowerCase();

		log.debug("Uploading logo '" + props.getProperty("settings.company.logo") + "." + ext + "' for company '" + company.getName() + "' to '" + compPath + "'");
		
		//delete the old icon file if it exists
		if (company.getSettings().getLogoPath() != null) {
			log.debug("The file already exists. Deleting");
			String[] oldFileName = company.getSettings().getLogoPath().split(File.separator);
			File oldFile = new File(compPath + File.separator + oldFileName[oldFileName.length - 1]);
			oldFile.delete();
		}
		
		//create the file
		String newLogoFilePath = compPath + File.separator + props.getProperty("settings.company.logo") + "." + ext;
		String newLogoPath = "";
		createFile(imageBytes, newLogoFilePath);
		newLogoPath = props.getProperty("settings.company.basepath") + File.separator + company.getName().toLowerCase() + File.separator + props.getProperty("settings.company.logo") + "." + ext;
		log.debug("Successfully uploaded icon. Its URL is at '" + newLogoPath + "'");
		return newLogoPath;
	}
	
	/**
	 * Uploads a temporary file to a predefined temp path
	 * @param fileBytes the bytes of the file
	 * @param filename the name of the file
	 * @return the full path including the name
	 */
	public static String uploadTmpFile(byte[] fileBytes, String filename) {
		Properties props = new WMProperties("application.properties");
		filename = filename.replaceAll("[^a-zA-Z0-9.]", ""); //get rid of special chars and spaces to avoid errors
		String tmpFilePath = Constants.BASE_PATH + props.getProperty("tmp.path") + File.separator + filename;
		log.debug("Uploading temporary file '" + filename + "' to '" + tmpFilePath + "'");
		
		//create the file
		try {
			createFile(fileBytes, tmpFilePath);
		} catch (IOException e) {
			return e.getMessage();
		}
		
		log.debug("Successfully uploaded temporary file.");
		return props.getProperty("tmp.basepath") + File.separator + filename;
	}
	
	/**
	 * Creates a file at the given path
	 * @param fileBytes the bytes of the file
	 * @param path the path to create the file at
	 * @throws IOException if an error occured during the writing process
	 */
	private static void createFile(byte[] fileBytes, String path) throws IOException {
		//create the file
		File file = new File(path);
		try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			outputStream.write(fileBytes);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new IOException(messageProps.getProperty("error.file_upload"));
		}
	}
}
