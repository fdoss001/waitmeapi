DROP procedure IF EXISTS `employee_permission_del_all_employee`;
DELIMITER $$
CREATE PROCEDURE `employee_permission_del_all_employee`(
IN uid INT(10))
BEGIN
	DELETE FROM employee_permission
    WHERE employee_id = uid;
END$$
DELIMITER ;
