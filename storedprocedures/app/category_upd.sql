DROP procedure IF EXISTS `category_upd`;
DELIMITER $$
CREATE PROCEDURE `category_upd`(
IN cid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE category SET
	category_code = co,
	category_name = na,
	category_active = ac,
	category_lastupdate_dtm = CURRENT_TIMESTAMP,
	category_lastupdate_user_id = luuid
	WHERE category_id = cid;
END$$
DELIMITER ;
