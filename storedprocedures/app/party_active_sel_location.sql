DROP procedure IF EXISTS `party_active_sel_location`;
DELIMITER $$
CREATE PROCEDURE `party_active_sel_location`(
IN lid INT(7)
)
BEGIN
	SELECT * FROM party
	WHERE party_active = TRUE AND party_table_restaurant_id IN (
		SELECT table_restaurant_id FROM table_restaurant
		WHERE table_restaurant_location_id = lid);
END$$
DELIMITER ;
