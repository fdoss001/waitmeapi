DROP procedure IF EXISTS `menu_sel_min_location`;
DELIMITER $$
CREATE PROCEDURE `menu_sel_min_location`(
IN lid INT(7))
BEGIN
	SELECT menu_id, menu_code, menu_name, menu_active FROM menu WHERE menu_location_id = lid
	ORDER BY menu_name;
END$$
DELIMITER ;
