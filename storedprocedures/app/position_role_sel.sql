DROP procedure IF EXISTS `position_role_sel`;
DELIMITER $$
CREATE PROCEDURE `position_role_sel`(
IN pid TINYINT(3))
BEGIN
	SELECT p.*, l.location_id, l.location_name FROM position_role p
	INNER JOIN location l ON l.location_id = p.position_role_location_id
	WHERE p.position_role_id = pid;
END$$
DELIMITER ;
