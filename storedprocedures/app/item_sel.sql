DROP procedure IF EXISTS `item_sel`;
DELIMITER $$
CREATE PROCEDURE `item_sel`(
IN iid SMALLINT(4))
BEGIN
	SELECT i.*, n.* FROM item i
	INNER JOIN nutrition_facts n ON i.item_nutrition_facts_id = n.nutrition_facts_id
	WHERE item_id = iid
	ORDER BY item_name;
END$$
DELIMITER ;
