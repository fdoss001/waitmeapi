DROP procedure IF EXISTS `meal_sel`;
DELIMITER $$
CREATE PROCEDURE `meal_sel`(
IN mid SMALLINT(4))
BEGIN
	SELECT * FROM meal
	WHERE meal_id = mid
	ORDER BY meal_name;
END$$
DELIMITER ;
