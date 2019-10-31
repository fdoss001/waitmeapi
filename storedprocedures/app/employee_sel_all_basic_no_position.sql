DROP procedure IF EXISTS `employee_sel_all_basic_no_position`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_all_basic_no_position`()
BEGIN
	SELECT employee_id, employee_uname, employee_fname, employee_lname FROM employee
	WHERE employee_position_role_id = 0;
END$$
DELIMITER ;
