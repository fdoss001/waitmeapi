DROP procedure IF EXISTS `location_sel`;
DELIMITER $$
CREATE PROCEDURE `location_sel`(
IN lid INT(7))
BEGIN
	SELECT * FROM location
	WHERE location_id = lid;
END$$
DELIMITER ;
