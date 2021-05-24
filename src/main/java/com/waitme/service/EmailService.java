package com.waitme.service;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.waitme.config.Constants;
import com.waitme.utils.WMLogger;

//TODO fix email template resources
/**
 * Service class for sending emails
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-08
 * @since 1.0 2019-01-18
 */
@Service
public class EmailService {	
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private TemplateEngine htmlTemplateEngine;
	private WMLogger log = new WMLogger(EmailService.class);
	
	/**
	 * Sends a password reset email
	 * @param recipientName the name of the recipient to include in the email
	 * @param recipientEmail the email address of the recipient
	 * @param newPasswd the new password for the recipient's login
	 * @param uname the username of the recipient
	 */
	public void sendPasswordResetEmail(final String recipientName, final String recipientEmail, final String newPasswd, final String uname, final Locale locale) throws MessagingException {
		log.debug("Preparing to send password reset email to '" + recipientEmail + "'");
		//Prepare context
		final Context context = new Context(locale);
		context.setVariable("name", recipientName);
		context.setVariable("email", recipientEmail);
		context.setVariable("newPasswd", newPasswd);
		context.setVariable("uname", uname);
		context.setVariable("logo", "logo");
		
		//prepare and send message
		final MimeMessage mimeMessage = prepareMessage("Password Reset Information", recipientEmail, Constants.Email.FORGOT_PASSWORD_TEMPLATE_NAME, context);		
		mailSender.send(mimeMessage);
		log.debug("Password reset email successfully sent.");
	}
	
	/**
	 * Sends a new user email with new account setup instructions
	 * @param recipientName the name of the recipient to include in the email
	 * @param recipientEmail the email address of the recipient
	 * @param newUname the username of the recipient
	 * @param newPasswd the new password for the recipient's login
	 * @param newPin the new pin for the recipient's POS login
	 */
	public void sendNewUserEmail(final String recipientName, final String recipientEmail, final String newUname, final String newPasswd, final String newPin, final Locale locale) throws MessagingException {
		log.debug("Preparing to send new account email to '" + recipientEmail + "'");
		//Prepare context
		final Context context = new Context(locale);
		context.setVariable("name", recipientName);
		context.setVariable("email", recipientEmail);
		context.setVariable("newUname", newUname);
		context.setVariable("newPasswd", newPasswd);
		context.setVariable("newPin", newPin);
		context.setVariable("logo", "logo");
		
		//prepare and send message
		final MimeMessage mimeMessage = prepareMessage("Welcome to WaitMe", recipientEmail, Constants.Email.NEW_USER_TEMPLATE_NAME, context);
		mailSender.send(mimeMessage);
		log.debug("New account email successfully sent.");
	}
	
	public void sendMailWithInline(final String recipientName, final String recipientEmail, final String imageResourceName, final byte[] imageBytes, final String imageContentType, final Locale locale) throws MessagingException {
		
	}
	
	//prepares a default message
	private final MimeMessage prepareMessage(String subject, String recipientEmail, String template, final Context context) throws MessagingException {
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setFrom("noreply@waitme.com");
		message.setTo(recipientEmail);
		message.setSubject(subject);
		
		//Create HTML body using Thymeleaf
		final String htmlContent = htmlTemplateEngine.process(template, context);
		message.setText(htmlContent, true);
		message.addInline("logo", new ClassPathResource(Constants.Email.LOGO_PATH), "image/png");
		return mimeMessage;
	}
}
