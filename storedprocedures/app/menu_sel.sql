DROP procedure IF EXISTS `menu_sel`;
DELIMITER $$
CREATE PROCEDURE `menu_sel`(
IN mid INT(7))
BEGIN
	SELECT * FROM menu WHERE menu_id = mid
	ORDER BY menu_name;
END$$
DELIMITER ;
