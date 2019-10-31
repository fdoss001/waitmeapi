DROP procedure IF EXISTS `employee_del`;
DELIMITER $$
CREATE PROCEDURE `employee_del`(
IN uid INT(10))
BEGIN
	SET @count = 0;
	
	-- Verify that the employee has no party history
	SELECT COUNT(*) INTO @count FROM party
	WHERE party_employee_id = uid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This employee has party and order history.";
	END IF;
	
	SET @un = '';
	SELECT employee_uname INTO @un FROM employee WHERE employee_id = uid;
	
	DELETE FROM waitme.company_user
	WHERE company_user_uname = @un;
	
	DELETE FROM employee
	WHERE employee_id = uid;
	
	DELETE FROM employee_sub_module
	WHERE employee_id = uid;
	
	DELETE FROM timesheet_day
	WHERE timesheet_day_employee_id = uid;
	
	DELETE FROM employee_settings
	WHERE employee_settings_employee_id = uid;
	
	DELETE FROM employee_authorized_location
	WHERE employee_id = uid;
	
	DELETE FROM employee_permission
	WHERE employee_id = uid;
END$$
DELIMITER ;
