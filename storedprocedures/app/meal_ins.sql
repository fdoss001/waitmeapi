DROP procedure IF EXISTS `meal_ins`;
DELIMITER $$
CREATE PROCEDURE `meal_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	INSERT INTO meal (meal_code, meal_name, meal_active, meal_lastupdate_user_id)
	VALUES (co, na, ac, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
