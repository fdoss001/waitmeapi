DROP procedure IF EXISTS `sub_category_meal_del_all_sub_category`;
DELIMITER $$
CREATE PROCEDURE `sub_category_meal_del_all_sub_category`(
IN scid SMALLINT(4))
BEGIN
	DELETE FROM sub_category_meal WHERE sub_category_id = scid;
END$$
DELIMITER ;
