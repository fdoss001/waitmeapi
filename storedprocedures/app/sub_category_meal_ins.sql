DROP procedure IF EXISTS `sub_category_meal_ins`;
DELIMITER $$
CREATE PROCEDURE `sub_category_meal_ins`(
IN scid SMALLINT(4),
IN mid SMALLINT(4))
BEGIN
	INSERT INTO sub_category_meal (sub_category_id, meal_id)
	VALUES (scid, mid);
END$$
DELIMITER ;
