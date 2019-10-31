DROP procedure IF EXISTS `employee_loginout_pos`;
DELIMITER $$
CREATE PROCEDURE `employee_loginout_pos`(
IN uid INT(10),
IN li TINYINT(1))
BEGIN
	
    -- Check if this user has ever been clocked in
    SET @clockcount = (SELECT COUNT(*) FROM timesheet_day
    WHERE timesheet_day_employee_id = uid);
    -- Check if there is a clock in that was never clocked out
	SET @lastout = (SELECT ANY_VALUE(timesheet_day_dtm_out) FROM timesheet_day 
    WHERE timesheet_day_employee_id = uid
    GROUP BY timesheet_day_dtm_in
    ORDER BY timesheet_day_dtm_in DESC LIMIT 1);
    
	IF (@lastout IS NOT NULL AND li = 1) OR @clockcount = 0 THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'User is not clocked in';
	END IF; 
    
	UPDATE employee SET employee_loggedin_pos = li 
    WHERE employee_id = uid;
    
    IF li = 1 THEN
		UPDATE employee SET employee_lastlogin_pos_dtm = CURRENT_TIMESTAMP
        WHERE employee_id = uid;
	END IF;
END$$
DELIMITER ;
