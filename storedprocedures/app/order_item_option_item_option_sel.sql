DROP procedure IF EXISTS `order_item_option_item_option_sel`;
DELIMITER $$
CREATE PROCEDURE `order_item_option_item_option_sel`(
IN oiid INT(11))
BEGIN
	SELECT io.*, oio.*, n.* FROM item_option io
	INNER JOIN order_item_option oio ON io.item_option_id = oio.item_option_id
	INNER JOIN nutrition_facts n ON io.item_option_nutrition_facts_id = n.nutrition_facts_id
	WHERE oio.order_item_id = oiid;
END$$
DELIMITER ;
