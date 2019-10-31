DROP procedure IF EXISTS `item_option_sel`;
DELIMITER $$
CREATE PROCEDURE `item_option_sel`(
IN ioid SMALLINT(4))
BEGIN
	SELECT io.*, n.* FROM item_option io
	INNER JOIN nutrition_facts n ON io.item_option_nutrition_facts_id = n.nutrition_facts_id
	WHERE item_option_id = ioid
	ORDER BY item_option_name;
END$$
DELIMITER ;
