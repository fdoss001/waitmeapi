DROP procedure IF EXISTS `timesheet_day_sel_all_week`;
DELIMITER $$
CREATE PROCEDURE `timesheet_day_sel_all_week`(
IN uid INT(10),
IN day DATE)
BEGIN
	SELECT * FROM timesheet_day
	WHERE timesheet_day_employee_id = uid
	AND timesheet_day_weekstart = day;
END$$
DELIMITER ;
