DROP procedure IF EXISTS `employee_sel_all_basic_location`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_all_basic_location`(
IN lid INT(7))
BEGIN
	SELECT e.employee_id, e.employee_uname, e.employee_fname, e.employee_lname, e.employee_active, 
	pr.position_role_id, pr.position_role_code, pr.position_role_name FROM employee e
	INNER JOIN position_role pr ON pr.position_role_id = e.employee_position_role_id
	WHERE pr.position_role_location_id = lid;
END$$
DELIMITER ;
