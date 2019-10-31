DROP procedure IF EXISTS `meal_sel_all_min`;
DELIMITER $$
CREATE PROCEDURE `meal_sel_all_min`()
BEGIN
	SELECT meal_id, meal_code, meal_name, meal_active FROM meal
	ORDER BY meal_name;
END$$
DELIMITER ;
