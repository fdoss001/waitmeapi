DROP procedure IF EXISTS `sub_category_del`;
DELIMITER $$
CREATE PROCEDURE `sub_category_del`(
IN scid SMALLINT(4))
BEGIN
	DELETE FROM sub_category WHERE sub_category_id = scid;
	DELETE FROM category_sub_category WHERE sub_category_id = scid;
	DELETE FROM sub_category_meal WHERE sub_category_id = scid;
END$$
DELIMITER ;
