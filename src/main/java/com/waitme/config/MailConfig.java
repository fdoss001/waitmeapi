package com.waitme.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration setup for sending email including resolvers
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-08 
 * @since 1.0 2019-01-18
 */
@Configuration
public class MailConfig {
	
//	@Bean
//	public JavaMailSender getJavaMailSender() throws IOException {
//		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//		final Properties emailProps = new WMProperties("mail/emailconfig.properties");
//		final Properties javaMailProps = new WMProperties("mail/javamail.properties");
//		
//		//mail sender config
//		mailSender.setHost(emailProps.getProperty("mail.host"));
//		mailSender.setPort(Integer.parseInt(emailProps.getProperty("mail.port")));
//		mailSender.setUsername(emailProps.getProperty("mail.username"));
//		mailSender.setPassword(emailProps.getProperty("mail.password"));
//		
//		
//		final Properties mailProps = mailSender.getJavaMailProperties();
//		mailProps.load(((WMProperties) javaMailProps).getInputStream()); //will not throw exception
//		mailSender.setJavaMailProperties(mailProps);
//		
//		return mailSender;
//	}
	
//	@Bean
//	public ResourceBundleMessageSource emailMessageSource() {
//		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//		messageSource.setBasename("mail/MailMessages");
//		return messageSource;
//	}
//	
//	@Bean
//	public TemplateEngine emaiTemplateEngine() {
//		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//		//Resolver for HTML emails
//		templateEngine.addTemplateResolver(htmlTemplateResolver());
//		return templateEngine;
//	}
//	
//	private ITemplateResolver htmlTemplateResolver() {
//		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//		templateResolver.setOrder(2);
//		templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
//		templateResolver.setPrefix("/mail/");
//		templateResolver.setSuffix(".html");
//		templateResolver.setTemplateMode(TemplateMode.HTML);
//		templateResolver.setCharacterEncoding("UTF-8");
//		templateResolver.setCacheable(false);
//		return templateResolver;
//	}
}
