DROP procedure IF EXISTS `location_upd`;
DELIMITER $$
CREATE PROCEDURE `location_upd`(
IN lid INT(7),
IN na VARCHAR(45),
IN ty ENUM('casual_dining'),
IN ad TEXT,
IN ph VARCHAR(20),
IN dto VARCHAR(153),
IN uid INT(10))
BEGIN
	UPDATE location SET
	location_name = na,
	location_type = ty,
	location_address = ad,
	location_phone = ph,
	location_dtms_open = dto,
	location_lastupdate_user_id = uid,
	location_lastupdate_dtm = CURRENT_TIMESTAMP
	WHERE location_id = lid;
END$$
DELIMITER ;
