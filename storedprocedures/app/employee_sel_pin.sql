DROP procedure IF EXISTS `employee_sel_pin`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_pin`(
IN p INT(10))
BEGIN
	SELECT e.*, es.*, pr.*, td.*, c.*, cs.*, l.location_id, l.location_name FROM employee e
	INNER JOIN employee_settings es ON e.employee_id = es.employee_settings_employee_id
	LEFT JOIN position_role pr ON pr.position_role_id = e.employee_position_role_id
	LEFT JOIN timesheet_day td ON td.timesheet_day_employee_id = e.employee_id AND td.timesheet_day_dtm_out IS NULL
	INNER JOIN waitme.company c ON c.company_id = e.employee_company_id
	INNER JOIN waitme.company_settings cs ON cs.company_id = e.employee_company_id
	LEFT JOIN location l ON l.location_id = es.employee_settings_current_location_id
	WHERE e.employee_pin = p;
END$$
DELIMITER ;
