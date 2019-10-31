DROP procedure IF EXISTS `table_restaurant_active_sel_location`;
DELIMITER $$
CREATE PROCEDURE `table_restaurant_active_sel_location`(
IN lid INT(7))
BEGIN
	SELECT * FROM table_restaurant
	WHERE table_restaurant_location_id = lid
	AND table_restaurant_active = TRUE;
END$$
DELIMITER ;
