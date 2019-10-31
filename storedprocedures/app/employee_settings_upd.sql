DROP procedure IF EXISTS `employee_settings_upd`;
DELIMITER $$
CREATE PROCEDURE `employee_settings_upd`(
IN uid INT(10),
IN tp VARCHAR(255),
IN dpm SMALLINT(4))
BEGIN
	IF dpm < 0 THEN
		UPDATE employee_settings SET
		employee_settings_theme_path = tp,
		employee_settings_default_pos_sub_module_id = null
	    WHERE employee_settings_employee_id = uid;
    ELSE
		UPDATE employee_settings SET
		employee_settings_theme_path = tp,
		employee_settings_default_pos_sub_module_id = dpm
	    WHERE employee_settings_employee_id = uid;
    END IF;
END$$
DELIMITER ;
