DROP procedure IF EXISTS `employee_settings_ins`;
DELIMITER $$
CREATE PROCEDURE `employee_settings_ins`(
IN eid INT(10),
IN dpm SMALLINT(4))
BEGIN
	INSERT INTO employee_settings (employee_settings_employee_id, employee_settings_default_pos_sub_module_id) VALUES (eid, dpm);
END$$
DELIMITER ;
