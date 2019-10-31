DROP procedure IF EXISTS `meal_item_sel`;
DELIMITER $$
CREATE PROCEDURE `meal_item_sel`(
IN mid SMALLINT(4))
BEGIN
	SELECT i.*, n.*, mi.* FROM item i
	INNER JOIN nutrition_facts n ON n.nutrition_facts_id = i.item_nutrition_facts_id
	INNER JOIN meal_item mi ON mi.item_id = i.item_id
	WHERE mi.meal_id = mid;
END$$
DELIMITER ;