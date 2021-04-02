DROP procedure IF EXISTS `item_option_tog_act`;
DELIMITER $$
CREATE PROCEDURE `item_option_tog_act`(
IN ioid SMALLINT(4),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE item_option SET
	item_option_active = ac,
	item_option_lastupdate_dtm = CURRENT_TIMESTAMP,
	item_option_lastupdate_user_id = luuid
	WHERE item_option_id = ioid;
END$$
DELIMITER ;