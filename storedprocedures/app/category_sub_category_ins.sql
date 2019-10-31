DROP procedure IF EXISTS `category_sub_category_ins`;
DELIMITER $$
CREATE PROCEDURE `category_sub_category_ins`(
IN cid SMALLINT(4),
IN scid SMALLINT(4))
BEGIN
	INSERT INTO category_sub_category (category_id, sub_category_id)
	VALUES (cid, scid);
END$$
DELIMITER ;
