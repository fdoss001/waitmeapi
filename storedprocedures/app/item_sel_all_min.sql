DROP procedure IF EXISTS `item_sel_all_min`;
DELIMITER $$
CREATE PROCEDURE `item_sel_all_min`()
BEGIN
	SELECT item_id, item_code, item_name, item_active, item_base_price FROM item
	ORDER BY item_name;
END$$
DELIMITER ;
