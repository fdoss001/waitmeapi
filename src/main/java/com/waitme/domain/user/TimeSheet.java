package com.waitme.domain.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;

/**
 * Class to represent an employee's timesheet
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-19
 * @since 1.0 2019-02-19
 */
public class TimeSheet extends WMDomainObject implements RowMapper<TimeSheet> {
	private int employeeId, locationId;
	private Date startOfWeek, timeIn, timeOut, breakStart, breakEnd;
	
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(TimeSheet.class);
	
	public TimeSheet() {}
	
	public TimeSheet(int employeeId, int locationId, Date startOfWeek, Date timeIn, Date timeOut, Date breakOut, Date breakIn) {
		this.employeeId = employeeId;
		this.locationId = locationId;
		this.startOfWeek = startOfWeek;
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.breakStart = breakOut;
		this.breakEnd = breakIn;
	}

	public Date getStartOfWeek() {
		return startOfWeek;
	}

	public void setStartOfWeek(Date startOfWeek) {
		this.startOfWeek = startOfWeek;
	}

	public Date getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(Date timeIn) {
		this.timeIn = timeIn;
	}

	public Date getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Date timeOut) {
		this.timeOut = timeOut;
	}

	public Date getBreakStart() {
		return breakStart;
	}

	public void setBreakStart(Date breakOut) {
		this.breakStart = breakOut;
	}

	public Date getBreakEnd() {
		return breakEnd;
	}

	public void setBreakEnd(Date breakIn) {
		this.breakEnd = breakIn;
	}

	public int getEmployeeId() {
		return employeeId;
	}
	
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
	@Override
	public TimeSheet mapRow(ResultSet rs, int rowNum) throws SQLException {
		TimeSheet timesheetDay = new TimeSheet();
		
		try {timesheetDay.setEmployeeId(rs.getInt("timesheet_day_employee_id"));} catch(SQLException e) {log.error("No employeeid for timesheet. Not found.");}
		try {timesheetDay.setLocationId(rs.getInt("timesheet_day_location_id"));} catch(SQLException e) {log.debug("No locationid for timesheet '" + timesheetDay.getEmployeeId() + "'");}
		try {timesheetDay.setStartOfWeek(rs.getDate("timesheet_day_weekstart"));} catch(SQLException e) {log.debug("No weekstart for timesheet '" + timesheetDay.getEmployeeId() + "'");}
		try {timesheetDay.setTimeIn(rs.getTimestamp("timesheet_day_dtm_in"));} catch(SQLException e) {log.debug("No dtm_in for timesheet '" + timesheetDay.getEmployeeId() + "'");}
		try {timesheetDay.setTimeOut(rs.getTimestamp("timesheet_day_dtm_out"));} catch(SQLException e) {log.debug("No dtm_out for timesheet '" + timesheetDay.getEmployeeId() + "'");}
		try {timesheetDay.setBreakStart(rs.getTimestamp("timesheet_day_break_start"));} catch(SQLException e) {log.debug("No break_start for timesheet '" + timesheetDay.getEmployeeId() + "'");}
		try {timesheetDay.setBreakEnd(rs.getTimestamp("timesheet_day_break_end"));} catch(SQLException e) {log.debug("No break_end for timesheet '" + timesheetDay.getEmployeeId() + "'");}
		
		return timesheetDay;
	}
}
