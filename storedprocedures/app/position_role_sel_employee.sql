DROP procedure IF EXISTS `position_role_sel_employee`;
DELIMITER $$
CREATE PROCEDURE `position_role_sel_employee`(
IN eid INT(10))
BEGIN
	SELECT p.*, l.location_id, l.location_name FROM position_role p
	INNER JOIN location l ON l.location_id = p.position_role_location_id
	WHERE p.position_role_id = 
		(SELECT employee_position_role_id FROM employee
		WHERE employee_id = eid);
END$$
DELIMITER ;
