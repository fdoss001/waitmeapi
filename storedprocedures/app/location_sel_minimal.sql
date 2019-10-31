DROP procedure IF EXISTS `location_sel_minimal`;
DELIMITER $$
CREATE PROCEDURE `location_sel_minimal`(
IN lid INT(7))
BEGIN
	SELECT location_id, location_name FROM location
	WHERE location_id = lid;
END$$
DELIMITER ;
