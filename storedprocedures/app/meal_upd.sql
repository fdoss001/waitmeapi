DROP procedure IF EXISTS `meal_upd`;
DELIMITER $$
CREATE PROCEDURE `meal_upd`(
IN mid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE meal SET
	meal_code = co,
	meal_name = na,
	meal_active = ac,
	meal_lastupdate_dtm = CURRENT_TIMESTAMP,
	meal_lastupdate_user_id = luuid
	WHERE meal_id = mid;
END$$
DELIMITER ;
