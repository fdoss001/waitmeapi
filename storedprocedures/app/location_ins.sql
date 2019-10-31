DROP procedure IF EXISTS `location_ins`;
DELIMITER $$
CREATE PROCEDURE `location_ins`(
IN na VARCHAR(45),
IN ty ENUM('casual_dining'),
IN ad TEXT,
IN ph VARCHAR(20),
IN dto VARCHAR(153),
IN uid INT(10))
BEGIN
	INSERT INTO location (location_name, location_type, location_address, location_phone, location_dtms_open, location_lastupdate_user_id)
	VALUES (na, ty, ad, ph, dto, uid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
