DROP procedure IF EXISTS `item_item_option_sel_all_arr`;
DELIMITER $$
CREATE PROCEDURE `item_item_option_sel_all_arr`(
IN idarr VARCHAR(255))
BEGIN
	SELECT io.*, n.*, 'normal' AS item_item_option_quantity, 0.0 AS item_item_option_price_adjustment FROM item_option io
	INNER JOIN nutrition_facts n ON io.item_option_nutrition_facts_id = n.nutrition_facts_id
	WHERE FIND_IN_SET(io.item_option_id, idarr);
END$$
DELIMITER ;
