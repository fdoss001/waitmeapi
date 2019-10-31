DROP procedure IF EXISTS `menu_ins`;
DELIMITER $$
CREATE PROCEDURE `menu_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN dta VARCHAR(153),
IN lid INT(7),
IN luuid INT(10))
BEGIN
	INSERT INTO menu (menu_code, menu_name, menu_active, menu_dtms_available, menu_location_id, menu_lastupdate_user_id)
	VALUES (co, na, ac, dta, lid, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
