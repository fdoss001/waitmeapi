DROP procedure IF EXISTS `location_sel_all_minimal`;
DELIMITER $$
CREATE PROCEDURE `location_sel_all_minimal`()
BEGIN
	SELECT location_id, location_name FROM location;
END$$
DELIMITER ;
