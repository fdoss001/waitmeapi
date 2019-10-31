DROP procedure IF EXISTS `category_sub_category_del_all_category`;
DELIMITER $$
CREATE PROCEDURE `category_sub_category_del_all_category`(
IN cid SMALLINT(4))
BEGIN
	DELETE FROM category_sub_category WHERE category_id=cid;
END$$
DELIMITER ;
