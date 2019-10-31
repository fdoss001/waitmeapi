DROP procedure IF EXISTS `item_item_option_sel`;
DELIMITER $$
CREATE PROCEDURE `item_item_option_sel`(
IN iid SMALLINT(4))
BEGIN
	SELECT io.*, ioi.*, n.* FROM item_option io
	INNER JOIN nutrition_facts n ON io.item_option_nutrition_facts_id = n.nutrition_facts_id
	INNER JOIN item_item_option ioi ON io.item_option_id = ioi.item_option_id
	WHERE ioi.item_id = iid;
END$$
DELIMITER ;
