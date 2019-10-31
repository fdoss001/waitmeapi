DROP procedure IF EXISTS `employee_sel_all_pin_basic_location`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_all_pin_basic_location`(
IN lid INT(7))
BEGIN
	SELECT employee_id, employee_pin FROM employee e
	INNER JOIN position_role pr ON pr.position_role_id = e.employee_position_role_id
	WHERE pr.position_role_location_id = lid;
END$$
DELIMITER ;
