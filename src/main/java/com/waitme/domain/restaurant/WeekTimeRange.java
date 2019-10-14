package com.waitme.domain.restaurant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.waitme.domain.WMDomainObject;

/**
 * Class to keep a span of time ranges for a week
 * This is used to keep the days and times that something is available or open
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-27
 * @since 1.0 2019-02-27
 */
public class WeekTimeRange extends WMDomainObject {

	TimeRange sunday, monday, tuesday, wednesday, thursday, friday, saturday;
	
	public WeekTimeRange() {} //dummy constructor for json mapping

	/**
	 * Builds a new week time range object from a formatted string.
	 * WARNING: There is no format check so make sure it adheres to the following format:
	 * DDD, HH:MM-DDD, HH:MM;DDD, HH:MM-DDD, HH:MM;...
	 * The format is a 3 letter day such as Sun or Mon. This is followed by a comma, space, then 2 digit hour and minute. This is the open time
	 * Then there is a dash and the same format again for the close time.To separate the next day, a semi-colon is used. 
	 * The entire string is not ended with a semi-colon
	 * @param dtmsRange the formatted string of date time ranges for the week
	 */
	public WeekTimeRange(String dtmsRange) {
		String[] dtmsAvlStrArr = dtmsRange.split(";");
		
		for (String dtmr : dtmsAvlStrArr) {
			if (!dtmr.equals("null")) {
				String dayOfWeek = dtmr.substring(0, 3);
				String[] range = dtmr.split("-");
				Date start = null;
				Date end = null;
				try {
					start = new SimpleDateFormat("E, HH:mm").parse(range[0]);
					end = new SimpleDateFormat("E, HH:mm").parse(range[1]);
				} catch (ParseException e) {}//this won't happen because it will be validated in front end
				TimeRange day = new TimeRange(start, end);
				
				switch (dayOfWeek) {
				case "Sun": sunday = day; break;
				case "Mon": monday = day; break;
				case "Tue": tuesday = day; break;
				case "Wed": wednesday = day; break;
				case "Thu": thursday = day; break;
				case "Fri": friday = day; break;
				case "Sat": saturday = day; break;
				default: break;
				}
			}
		}
	}
	
	/**
	 * Builds a new week time range from the given formatted strings for the start and end times for each day of the week
	 * @param sunS Sunday start time
	 * @param sunE Sunday end time
	 * @param monS Monday start time
	 * @param monE Monday end time
	 * @param tueS Tuesday start time
	 * @param tueE Tuesday end time
	 * @param wedS Wednesday start time
	 * @param wedE Wednesday end time
	 * @param thuS Thursday start time
	 * @param thuE Thursday end time
	 * @param friS Friday start time
	 * @param friE Friday end time
	 * @param satS Saturday start time
	 * @param satE Saturday end time
	 */
	public WeekTimeRange(String sunS, String sunE, String monS, String monE, String tueS, String tueE, String wedS, String wedE, String thuS, String thuE, String friS, String friE, String satS, String satE) {
		Date start = null;
		Date end = null;
		
		TimeRange suntr = null;
		if (sunS != null && !sunS.isEmpty()) {
			try {
				start = new SimpleDateFormat("E, HH:mm").parse("Sun, " + sunS);
				end = new SimpleDateFormat("E, HH:mm").parse("Sun, " + sunE);
			} catch (ParseException e) {}//this won't happen because it will be validated in front end
			suntr = new TimeRange(start, end);
		}
		sunday = suntr;

		TimeRange montr = null;
		if (monS != null && !monS.isEmpty()) {
			try {
				start = new SimpleDateFormat("E, HH:mm").parse("Mon, " + monS);
				end = new SimpleDateFormat("E, HH:mm").parse("Mon, " + monE);
			} catch (ParseException e) {}//this won't happen because it will be validated in front end
			montr = new TimeRange(start, end);
		}
		monday = montr;

		TimeRange tuetr = null;
		if (tueS != null && !tueS.isEmpty()) {
			try {
				start = new SimpleDateFormat("E, HH:mm").parse("Tue, " + tueS);
				end = new SimpleDateFormat("E, HH:mm").parse("Tue, " + tueE);
			} catch (ParseException e) {}//this won't happen because it will be validated in front end
			tuetr = new TimeRange(start, end);
		}
		tuesday = tuetr;

		TimeRange wedtr = null;
		if (wedS != null && !wedS.isEmpty()) {
			try {
				start = new SimpleDateFormat("E, HH:mm").parse("Wed, " + wedS);
				end = new SimpleDateFormat("E, HH:mm").parse("Wed, " + wedE);
			} catch (ParseException e) {}//this won't happen because it will be validated in front end
			wedtr = new TimeRange(start, end);
		}
		wednesday = wedtr;

		TimeRange thutr = null;
		if (thuS != null && !thuS.isEmpty()) {
			try {
				start = new SimpleDateFormat("E, HH:mm").parse("Thu, " + thuS);
				end = new SimpleDateFormat("E, HH:mm").parse("Thu, " + thuE);
			} catch (ParseException e) {}//this won't happen because it will be validated in front end
			thutr = new TimeRange(start, end);
		}
		thursday = thutr;

		TimeRange fritr = null;
		if (friS != null && !friS.isEmpty()) {
			try {
				start = new SimpleDateFormat("E, HH:mm").parse("Fri, " + friS);
				end = new SimpleDateFormat("E, HH:mm").parse("Fri, " + friE);
			} catch (ParseException e) {}//this won't happen because it will be validated in front end
			fritr = new TimeRange(start, end);
		}
		friday = fritr;
		
		TimeRange sattr = null;
		if (satS != null && !satS.isEmpty()) {
			try {
				start = new SimpleDateFormat("E, HH:mm").parse("Sat, " + satS);
				end = new SimpleDateFormat("E, HH:mm").parse("Sat, " + satE);
			} catch (ParseException e) {}//this won't happen because it will be validated in front end
			sattr = new TimeRange(start, end);
		}
		saturday = sattr;
	}

	public TimeRange getSunday() {
		return sunday;
	}

	public void setSunday(TimeRange sunday) {
		this.sunday = sunday;
	}

	public TimeRange getMonday() {
		return monday;
	}

	public void setMonday(TimeRange monday) {
		this.monday = monday;
	}

	public TimeRange getTuesday() {
		return tuesday;
	}

	public void setTuesday(TimeRange tuesday) {
		this.tuesday = tuesday;
	}

	public TimeRange getWednesday() {
		return wednesday;
	}

	public void setWednesday(TimeRange wednesday) {
		this.wednesday = wednesday;
	}

	public TimeRange getThursday() {
		return thursday;
	}

	public void setThursday(TimeRange thursday) {
		this.thursday = thursday;
	}

	public TimeRange getFriday() {
		return friday;
	}

	public void setFriday(TimeRange friday) {
		this.friday = friday;
	}

	public TimeRange getSaturday() {
		return saturday;
	}

	public void setSaturday(TimeRange saturday) {
		this.saturday = saturday;
	}

	@Override
	public String toString() {
		return "" + (sunday != null ? sunday.toString() : "null") + ";" + (monday != null ? monday.toString() : "null") + ";" + (tuesday != null ? tuesday.toString() : "null") + ";" + (wednesday != null ? wednesday.toString() : "null") + ";" + (thursday != null ? thursday.toString() : "null") + ";" + (friday != null ? friday.toString() : "null") + ";" + (saturday != null ? saturday.toString() : "null"); 
	}
}
