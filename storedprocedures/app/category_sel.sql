DROP procedure IF EXISTS `category_sel`;
DELIMITER $$
CREATE PROCEDURE `category_sel`(
IN cid SMALLINT(4))
BEGIN
	SELECT * FROM category
	WHERE category_id = cid
	ORDER BY category_name;
END$$
DELIMITER ;
