DROP procedure IF EXISTS `employee_clockout`;
DELIMITER $$
CREATE PROCEDURE `employee_clockout`(
IN uid INT(10),
IN dto TIMESTAMP)
BEGIN    
    -- Check if there is a clock in that was never clocked out
    SET @dayid = 0;
	SET @lastout = '0000-00-00 00:00:00.0';
    SET @lastbreakin = '0000-00-00 00:00:00.0';
    SET @lastbreakout = '0000-00-00 00:00:00.0';
	SELECT timesheet_day_id, timesheet_day_dtm_out, timesheet_day_break_start, timesheet_day_break_end
	INTO @dayid, @lastout, @lastbreakstart, @lastbreakend
	FROM timesheet_day
	WHERE timesheet_day_employee_id = uid
	AND timesheet_day_dtm_out IS NULL
	ORDER BY timesheet_day_dtm_in DESC LIMIT 1;
    
    -- First, verify that an open day exists
    IF @lastout IS NOT NULL THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No clock in exists that was never clocked out.';
	END IF;
    
    /*Now we must check that if a start break exists, it was breaked out. 
    If it wasn't we must end the break.
    It's good practice to advise users to end break before clocking out*/
	IF @lastbreakin IS NOT NULL AND @lastbreakout IS NULL THEN
		UPDATE timesheet_day 
		SET timesheet_day_break_end = CURRENT_TIMESTAMP
		WHERE timesheet_day_id = @dayid;
	END IF;
    
    -- If everything was good above, update the latest clock entry
    UPDATE timesheet_day 
    SET timesheet_day_dtm_out = dto, timesheet_day_lastupdate_dtm = dto, timesheet_day_lastupdate_user_id = uid
    WHERE timesheet_day_id = @dayid;
END$$
DELIMITER ;
