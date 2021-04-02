DROP procedure IF EXISTS `item_tog_act`;
DELIMITER $$
CREATE PROCEDURE `item_tog_act`(
IN iid SMALLINT(4),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE item SET
	item_active = ac,
	item_lastupdate_dtm = CURRENT_TIMESTAMP,
	item_lastupdate_user_id = luuid
	WHERE item_id = iid;
END$$
DELIMITER ;