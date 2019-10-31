DROP procedure IF EXISTS `category_sel_all_min`;
DELIMITER $$
CREATE PROCEDURE `category_sel_all_min`()
BEGIN
	SELECT category_id, category_code, category_name, category_active FROM category
	ORDER BY category_name;
END$$
DELIMITER ;
