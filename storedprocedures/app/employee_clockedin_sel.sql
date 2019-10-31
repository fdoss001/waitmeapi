DROP procedure IF EXISTS `employee_clockedin_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_clockedin_sel`(
IN lid INT(7))
BEGIN
	SELECT e.employee_id, e.employee_uname, e.employee_fname, e.employee_lname, p.position_role_location_id FROM employee e
	INNER JOIN position_role p ON p.position_role_id = e.employee_position_role_id
	WHERE p.position_role_location_id = lid AND p.position_role_active = TRUE AND e.employee_active = TRUE AND
	e.employee_id IN (SELECT timesheet_day_employee_id  FROM timesheet_day
					WHERE timesheet_day_dtm_in IS NOT NULL AND timesheet_day_dtm_out IS NULL);
END$$
DELIMITER ;
