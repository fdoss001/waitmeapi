DROP procedure IF EXISTS `position_role_sel_all`;
DELIMITER $$
CREATE PROCEDURE `position_role_sel_all`()
BEGIN
	SELECT p.*, l.location_id, l.location_name FROM position_role p
	INNER JOIN location l ON l.location_id = p.position_role_location_id;
END$$
DELIMITER ;
