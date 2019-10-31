DROP procedure IF EXISTS `category_del`;
DELIMITER $$
CREATE PROCEDURE `category_del`(
IN cid SMALLINT(4))
BEGIN
	DELETE FROM category WHERE category_id=cid;
	DELETE FROM menu_category WHERE category_id=cid;
	DELETE FROM category_sub_category WHERE category_id=cid;
END$$
DELIMITER ;
