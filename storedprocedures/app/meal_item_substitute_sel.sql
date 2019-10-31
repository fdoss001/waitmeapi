DROP procedure IF EXISTS `meal_item_substitute_sel`;
DELIMITER $$
CREATE PROCEDURE `meal_item_substitute_sel`(
IN mid SMALLINT(4),
IN iid SMALLINT(4))
BEGIN
	SELECT i.*, n.*, mis.* FROM item i
	INNER JOIN nutrition_facts n ON i.item_nutrition_facts_id = n.nutrition_facts_id
	INNER JOIN meal_item_substitute mis ON mis.meal_item_substitute_id = i.item_id
	WHERE mis.meal_id = mid AND mis.item_id = iid;
END$$
DELIMITER ;