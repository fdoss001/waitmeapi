DROP procedure IF EXISTS `menu_upd`;
DELIMITER $$
CREATE PROCEDURE `menu_upd`(
IN mid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN dta VARCHAR(153),
IN lid INT(7),
IN luuid INT(10))
BEGIN
	UPDATE menu SET
	menu_code = co,
	menu_name = na,
	menu_active = ac,
	menu_dtms_available = dta, 
	menu_location_id = lid,
	menu_lastupdate_dtm = CURRENT_TIMESTAMP,
	menu_lastupdate_user_id = luuid
	WHERE menu_id = mid;
END$$
DELIMITER ;
