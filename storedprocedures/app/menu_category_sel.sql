DROP procedure IF EXISTS `menu_category_sel`;
DELIMITER $$
CREATE PROCEDURE `menu_category_sel`(
IN mid SMALLINT(4))
BEGIN
	SELECT c.* FROM category c
	INNER JOIN menu_category mc ON mc.category_id = c.category_id
	WHERE mc.menu_id = mid;
END$$
DELIMITER ;
