DROP procedure IF EXISTS `location_sel_all`;
DELIMITER $$
CREATE PROCEDURE `location_sel_all`()
BEGIN
	SELECT * FROM location;
END$$
DELIMITER ;
