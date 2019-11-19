DROP procedure IF EXISTS `employee_settings_ins`;
DELIMITER $$
CREATE PROCEDURE `employee_settings_ins`(
IN eid INT(10),
IN dpm SMALLINT(4),
IN clid INT(7))
BEGIN
	INSERT INTO employee_settings (employee_settings_employee_id, employee_settings_default_pos_sub_module_id, employee_settings_current_location_id)
	VALUES (eid, dpm, clid);
END$$
DELIMITER ;
