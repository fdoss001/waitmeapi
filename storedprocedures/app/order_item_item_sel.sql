DROP procedure IF EXISTS `order_item_item_sel`;
DELIMITER $$
CREATE PROCEDURE `order_item_item_sel`(
IN oid INT(11))
BEGIN
	SELECT i.*, n.*, oi.* FROM item i
	INNER JOIN nutrition_facts n ON i.item_nutrition_facts_id = n.nutrition_facts_id
	INNER JOIN order_item oi ON i.item_id = oi.item_id
	WHERE oi.order_id = oid;
END$$
DELIMITER ;
