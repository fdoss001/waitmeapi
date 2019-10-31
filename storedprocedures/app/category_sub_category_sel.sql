DROP procedure IF EXISTS `category_sub_category_sel`;
DELIMITER $$
CREATE PROCEDURE `category_sub_category_sel`(
IN cid SMALLINT(4))
BEGIN
	SELECT sc.* FROM sub_category sc
	INNER JOIN category_sub_category csc ON csc.sub_category_id = sc.sub_category_id
	WHERE csc.category_id=cid;
END$$
DELIMITER ;
