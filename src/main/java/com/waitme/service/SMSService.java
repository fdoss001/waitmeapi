package com.waitme.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import com.waitme.config.WMProperties;

/**
 * Service class for sending sms messages
 * @author Fernando Dos Santos
 * @version 1.0 2019-08-21
 * @since 1.0 2019-08-21
 */
public class SMSService {
	final Properties smsProps = new WMProperties("sms/sms.properties");

	public String sendSms() {
		try {
			// Construct data
			String data = "{"
					+ "\"messaging_profile_id\": \"" + smsProps.getProperty("telnyx.profile.id") + "\","
					+ "\"from\": \"" + smsProps.getProperty("telnyx.id") + "\","
					+ "\"to\": \"" + "+19544465979" + "\","
					+ "\"text\": \"" + "Test from WaitMe" + "\""
					+ "}";
//			String apiKey = "messaging_profile_id=" + smsProps.getProperty("telnyx.profile.id");
//			String message = "&text=" + "Test from WaitMe";
//			String sender = "&from=" + smsProps.getProperty("telnyx.id");
//			String numbers = "&to=" + "+19544465979";
			
			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.telnyx.com/v2/messages/alphanumeric_sender_id").openConnection();
//			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", "Bearer " + smsProps.getProperty("telenyx.api.key"));
			
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			
			return stringBuffer.toString();
		} catch (Exception e) {
			System.out.println("Error SMS "+e);
			return "Error "+e;
		}
	}
}
