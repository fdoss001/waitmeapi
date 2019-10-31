DROP procedure IF EXISTS `position_role_ins`;
DELIMITER $$
CREATE PROCEDURE `position_role_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ty ENUM('salary', 'hourly', 'hourly_tip', 'contract'),
IN bp DECIMAL(8,2),
IN lid INT(7),
IN uid INT(10))
BEGIN
	INSERT INTO position_role (position_role_code, position_role_name, position_role_type, position_role_base_pay, position_role_location_id, position_role_lastupdate_user_id)
	VALUES (co, na, ty, bp, lid, uid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
