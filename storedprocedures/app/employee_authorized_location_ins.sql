DROP procedure IF EXISTS `employee_authorized_location_ins`;
DELIMITER $$
CREATE PROCEDURE `employee_authorized_location_ins`(
IN uid INT(10),
IN lid INT(7))
BEGIN
	INSERT INTO employee_authorized_location (employee_id, location_id)
    VALUES (uid, lid);
END$$
DELIMITER ;
