DROP procedure IF EXISTS `meal_item_ins`;
DELIMITER $$
CREATE PROCEDURE `meal_item_ins`(
IN mid SMALLINT(4),
IN iid SMALLINT(4),
IN pa DECIMAL(8,2))
BEGIN
	IF pa IS NULL THEN
		INSERT INTO meal_item (meal_id, item_id)
		VALUES (mid, iid);
	ELSE
		INSERT INTO meal_item (meal_id, item_id, meal_item_price_adjustment)
		VALUES (mid, iid, pa);
	END IF;
END$$
DELIMITER ;
