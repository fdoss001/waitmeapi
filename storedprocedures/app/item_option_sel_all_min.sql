DROP procedure IF EXISTS `item_option_sel_all_min`;
DELIMITER $$
CREATE PROCEDURE `item_option_sel_all_min`()
BEGIN
	SELECT item_option_id, item_option_code, item_option_name, item_option_active, item_option_base_price FROM item_option
	ORDER BY item_option_name;
END$$
DELIMITER ;
