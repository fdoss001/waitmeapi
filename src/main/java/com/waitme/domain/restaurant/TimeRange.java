package com.waitme.domain.restaurant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.waitme.domain.WMDomainObject;

/**
 * Class to keep a span of time
 * This is used to keep the time something is available or open
 * for a particular day
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-27
 * @since 1.0 2019-02-27
 */
public class TimeRange extends WMDomainObject {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date start, end;
	
	public TimeRange() {} //dummy constructor for json mapping
	
	/**
	 * Initializes this range with a start and end time
	 * @param start the time it "opens"
	 * @param end the time it "closes"
	 */
	public TimeRange(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		if (start == null)
			return null;
		DateFormat dateFormat = new SimpleDateFormat("E, HH:mm");
		return dateFormat.format(start) + "-" + dateFormat.format(end);
	}
}
