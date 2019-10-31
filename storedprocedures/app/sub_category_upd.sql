DROP procedure IF EXISTS `sub_category_upd`;
DELIMITER $$
CREATE PROCEDURE `sub_category_upd`(
IN scid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE sub_category SET
	sub_category_code = co,
	sub_category_name = na,
	sub_category_active = ac,
	sub_category_lastupdate_dtm = CURRENT_TIMESTAMP,
	sub_category_lastupdate_user_id = luuid
	WHERE sub_category_id = scid;
END$$
DELIMITER ;
