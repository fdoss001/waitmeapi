DROP procedure IF EXISTS `sub_category_sel`;
DELIMITER $$
CREATE PROCEDURE `sub_category_sel`(
IN scid SMALLINT(4))
BEGIN
	SELECT * FROM sub_category
	WHERE sub_category_id = scid
	ORDER BY sub_category_name;
END$$
DELIMITER ;
