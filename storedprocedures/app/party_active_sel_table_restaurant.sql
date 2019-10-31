DROP procedure IF EXISTS `party_active_sel_table_restaurant`;
DELIMITER $$
CREATE PROCEDURE `party_active_sel_table_restaurant`(
IN tid INT(11)
)
BEGIN
	SELECT * FROM party WHERE party_table_restaurant_id = tid
	AND party_active = TRUE;
END$$
DELIMITER ;
