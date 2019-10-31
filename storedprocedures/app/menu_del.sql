DROP procedure IF EXISTS `menu_del`;
DELIMITER $$
CREATE PROCEDURE `menu_del`(
IN mid SMALLINT(4))
BEGIN
	DELETE FROM menu WHERE menu_id = mid;
	DELETE FROM menu_category WHERE menu_id = mid;
END$$
DELIMITER ;
