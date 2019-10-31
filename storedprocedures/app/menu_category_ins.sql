DROP procedure IF EXISTS `menu_category_ins`;
DELIMITER $$
CREATE PROCEDURE `menu_category_ins`(
IN mid SMALLINT(4),
IN cid SMALLINT(4))
BEGIN
	INSERT INTO menu_category (menu_id, category_id)
	VALUES (mid, cid);
END$$
DELIMITER ;
