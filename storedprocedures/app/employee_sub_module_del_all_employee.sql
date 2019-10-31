DROP procedure IF EXISTS `employee_sub_module_del_all_employee`;
DELIMITER $$
CREATE PROCEDURE `employee_sub_module_del_all_employee`(
IN eid INT(10))
BEGIN
	DELETE FROM employee_sub_module WHERE employee_id = eid;
END$$
DELIMITER ;
