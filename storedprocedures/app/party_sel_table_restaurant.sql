DROP procedure IF EXISTS `party_sel_table_restaurant`;
DELIMITER $$
CREATE PROCEDURE `party_sel_table_restaurant`(
IN tid INT(11)
)
BEGIN
	SELECT * FROM party WHERE party_table_restaurant_id = tid;
END$$
DELIMITER ;
