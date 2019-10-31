DROP procedure IF EXISTS `item_sel_all_arr`;
DELIMITER $$
CREATE PROCEDURE `item_sel_all_arr`(
IN idarr VARCHAR(255))
BEGIN
	SELECT i.*, n.* FROM item i
	INNER JOIN nutrition_facts n ON i.item_nutrition_facts_id = n.nutrition_facts_id
	WHERE FIND_IN_SET(i.item_id, idarr);
END$$
DELIMITER ;
