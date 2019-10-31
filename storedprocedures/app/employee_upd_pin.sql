DROP procedure IF EXISTS `employee_upd_pin`;
DELIMITER $$
CREATE PROCEDURE `employee_upd_pin`(
IN uid INT(10),
IN p INT(10))
BEGIN
	UPDATE employee SET
    employee_pin = p,
    employee_lastupdate_dtm = CURRENT_TIMESTAMP
    WHERE employee_id = uid;
END$$
DELIMITER ;
