DROP procedure IF EXISTS `position_role_upd`;
DELIMITER $$
CREATE PROCEDURE `position_role_upd`(
IN pid TINYINT(3),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ty ENUM('salary', 'hourly', 'hourly_tip', 'contract'),
IN bp DECIMAL(8,2),
IN lid INT(7),
IN uid INT(10))
BEGIN
	UPDATE position_role SET
	position_role_code = co,
	position_role_name = na,
	position_role_type = ty,
	position_role_base_pay = bp,
	position_role_location_id = lid,
	position_role_lastupdate_user_id = uid,
	position_role_lastupdate_dtm = CURRENT_TIMESTAMP
	WHERE position_role_id = pid;
END$$
DELIMITER ;
