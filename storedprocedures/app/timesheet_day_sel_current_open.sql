DROP procedure IF EXISTS `timesheet_day_sel_current_open`;
DELIMITER $$
CREATE PROCEDURE `timesheet_day_sel_current_open`(
IN uid INT(10))
BEGIN
	SELECT * FROM timesheet_day
	WHERE timesheet_day_employee_id = uid
	AND timesheet_day_dtm_out IS NULL
	ORDER BY timesheet_day_dtm_in DESC LIMIT 1;
END$$
DELIMITER ;
