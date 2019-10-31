DROP procedure IF EXISTS `meal_item_substitute_ins`;
DELIMITER $$
CREATE PROCEDURE `meal_item_substitute_ins`(
IN mid SMALLINT(4),
IN iid SMALLINT(4),
IN sid SMALLINT(4),
IN pa DECIMAL(8,2))
BEGIN
	INSERT INTO meal_item_substitute (meal_id, item_id, meal_item_substitute_id, meal_item_price_adjustment)
	VALUES (mid, iid, sid, pa);
END$$
DELIMITER ;
