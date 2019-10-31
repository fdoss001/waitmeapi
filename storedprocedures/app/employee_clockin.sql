DROP procedure IF EXISTS `employee_clockin`;
DELIMITER $$
CREATE PROCEDURE `employee_clockin`(
IN uid INT(10),
IN lid INT(7),
IN ws DATE,
IN dti TIMESTAMP)
BEGIN
    
    -- Check if there was a clock_out from last clock_in or if an entry exists
    SET @dayid = 0;
	SET @lastout = '0000-00-00 00:00:00.0';
    SELECT ANY_VALUE(timesheet_day_id), ANY_VALUE(timesheet_day_dtm_out) 
    INTO @dayid, @lastout 
    FROM timesheet_day 
    WHERE timesheet_day_employee_id = uid
    GROUP BY timesheet_day_dtm_in
    ORDER BY timesheet_day_dtm_in DESC LIMIT 1;
    
    IF @dayid IS NOT NULL AND @lastout IS NULL THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No clock out exists for latest clock in.';
	END IF;
    
    -- If everything was good above, make a new clock in entry
    INSERT INTO timesheet_day (timesheet_day_employee_id, timesheet_day_location_id, timesheet_day_weekstart, timesheet_day_dtm_in, timesheet_day_lastupdate_user_id)
    VALUES (uid, lid, ws, dti, uid);
END$$
DELIMITER ;
