DROP procedure IF EXISTS `menu_tog_act`;
DELIMITER $$
CREATE PROCEDURE `menu_tog_act`(
IN mid SMALLINT(4),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE menu SET
	menu_active = ac,
	menu_lastupdate_dtm = CURRENT_TIMESTAMP,
	menu_lastupdate_user_id = luuid
	WHERE menu_id = mid;
END$$
DELIMITER ;