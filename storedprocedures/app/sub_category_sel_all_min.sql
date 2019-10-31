DROP procedure IF EXISTS `sub_category_sel_all_min`;
DELIMITER $$
CREATE PROCEDURE `sub_category_sel_all_min`()
BEGIN
	SELECT sub_category_id, sub_category_code, sub_category_name, sub_category_active FROM sub_category
	ORDER BY sub_category_name;
END$$
DELIMITER ;
