DROP procedure IF EXISTS `employee_break_end`;
DELIMITER $$
CREATE PROCEDURE `employee_break_end`(
IN uid INT(10))
BEGIN    
    -- Check if there is a clock in that was never clocked out
    SET @dayid = 0;
	SET @lastout = '0000-00-00 00:00:00.0';
    SET @lastbreakin = '0000-00-00 00:00:00.0';
    SET @lastbreakout = '0000-00-00 00:00:00.0';
	SELECT ANY_VALUE(timesheet_day_id), ANY_VALUE(timesheet_day_dtm_out), ANY_VALUE(timesheet_day_break_start), ANY_VALUE(timesheet_day_break_end)
	INTO @dayid, @lastout, @lastbreakstart, @lastbreakend
    FROM timesheet_day
    WHERE timesheet_day_employee_id = uid
    GROUP BY timesheet_day_dtm_in
    ORDER BY timesheet_day_dtm_in DESC LIMIT 1;
    
    -- First verify that the day is open (clocked in without clock out)
    IF @lastout IS NOT NULL THEN
		SIGNAL SQLSTATE '45001'
        SET MESSAGE_TEXT = 'No open clock in exists to end break';
	END IF;
    
    -- Next verify that user has already started break
    IF @lastbreakstart IS NULL THEN
		SIGNAL SQLSTATE '45003'
        SET MESSAGE_TEXT = 'A start break does not exist for this day';
	END IF;
    
    -- Finally verify that user has not ended break yet
    IF @lastbreakend IS NOT NULL THEN
		SIGNAL SQLSTATE '45002'
        SET MESSAGE_TEXT = 'An end break already exists for this day';
	END IF;
    
    -- If everything was good above, update the latest break in entry
    UPDATE timesheet_day 
    SET timesheet_day_break_end = CURRENT_TIMESTAMP, timesheet_day_lastupdate_dtm = CURRENT_TIMESTAMP, timesheet_day_lastupdate_user_id = uid
    WHERE timesheet_day_id = @dayid;
END$$
DELIMITER ;
