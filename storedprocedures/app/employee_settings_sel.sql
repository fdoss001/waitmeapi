DROP procedure IF EXISTS `employee_settings_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_settings_sel`(
IN uid INT(10))
BEGIN
	SELECT * FROM employee_settings
	WHERE employee_settings_employee_id = uid;
END$$
DELIMITER ;
