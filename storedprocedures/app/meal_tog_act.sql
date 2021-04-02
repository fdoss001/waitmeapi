DROP procedure IF EXISTS `meal_tog_act`;
DELIMITER $$
CREATE PROCEDURE `meal_tog_act`(
IN mid SMALLINT(4),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE meal SET
	meal_active = ac,
	meal_lastupdate_dtm = CURRENT_TIMESTAMP,
	meal_lastupdate_user_id = luuid
	WHERE meal_id = mid;
END$$
DELIMITER ;