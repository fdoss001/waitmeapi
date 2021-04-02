DROP procedure IF EXISTS `sub_category_tog_act`;
DELIMITER $$
CREATE PROCEDURE `sub_category_tog_act`(
IN scid SMALLINT(4),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE sub_category SET
	sub_category_active = ac,
	sub_category_lastupdate_dtm = CURRENT_TIMESTAMP,
	sub_category_lastupdate_user_id = luuid
	WHERE sub_category_id = scid;
END$$
DELIMITER ;