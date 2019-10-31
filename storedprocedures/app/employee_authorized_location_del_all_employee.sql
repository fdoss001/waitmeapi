DROP procedure IF EXISTS `employee_authorized_location_del_all_employee`;
DELIMITER $$
CREATE PROCEDURE `employee_authorized_location_del_all_employee`(
IN uid INT(10))
BEGIN    
	DELETE FROM employee_authorized_location
    WHERE employee_id = uid;
END$$
DELIMITER ;
