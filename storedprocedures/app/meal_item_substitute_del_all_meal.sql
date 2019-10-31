DROP procedure IF EXISTS `meal_item_substitute_del_all_meal`;
DELIMITER $$
CREATE PROCEDURE `meal_item_substitute_del_all_meal`(
IN mid SMALLINT(4))
BEGIN
	DELETE FROM meal_item_substitute WHERE meal_id = mid;
END$$
DELIMITER ;
