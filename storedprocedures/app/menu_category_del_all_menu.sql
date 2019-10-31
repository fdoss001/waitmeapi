DROP procedure IF EXISTS `menu_category_del_all_menu`;
DELIMITER $$
CREATE PROCEDURE `menu_category_del_all_menu`(
IN mid SMALLINT(4))
BEGIN
	DELETE FROM menu_category WHERE menu_id = mid;
END$$
DELIMITER ;
