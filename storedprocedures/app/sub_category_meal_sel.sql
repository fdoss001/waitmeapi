DROP procedure IF EXISTS `sub_category_meal_sel`;
DELIMITER $$
CREATE PROCEDURE `sub_category_meal_sel`(
IN scid SMALLINT(4))
BEGIN
	SELECT m.* FROM meal m
	INNER JOIN sub_category_meal scm ON scm.meal_id = m.meal_id
	WHERE scm.sub_category_id = scid;
END$$
DELIMITER ;
