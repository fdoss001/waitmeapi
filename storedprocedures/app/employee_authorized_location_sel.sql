DROP procedure IF EXISTS `employee_authorized_location_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_authorized_location_sel`(
IN uid INT(10))
BEGIN
	SELECT l.location_id, l.location_name FROM location l
	INNER JOIN employee_authorized_location e ON e.location_id = l.location_id
	WHERE e.employee_id = uid;
END$$
DELIMITER ;
