DROP procedure IF EXISTS `meal_del`;
DELIMITER $$
CREATE PROCEDURE `meal_del`(
IN mid SMALLINT(4))
BEGIN
	DELETE FROM meal WHERE meal_id = mid;
	DELETE FROM sub_category_meal WHERE meal_id = mid;
	DELETE FROM meal_item WHERE meal_id = mid;
	DELETE FROM meal_item_substitute WHERE meal_id = mid;
END$$
DELIMITER ;
