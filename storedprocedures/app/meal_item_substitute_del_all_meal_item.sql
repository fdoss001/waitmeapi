DROP procedure IF EXISTS `meal_item_substitute_del_all_meal_item`;
DELIMITER $$
CREATE PROCEDURE `meal_item_substitute_del_all_meal_item`(
IN mid SMALLINT(4),
IN iid SMALLINT(4))
BEGIN
	DELETE FROM meal_item_substitute WHERE meal_id = mid AND item_id = iid;
END$$
DELIMITER ;
