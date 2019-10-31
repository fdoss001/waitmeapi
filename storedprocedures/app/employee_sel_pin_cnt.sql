DROP procedure IF EXISTS `employee_sel_pin_cnt`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_pin_cnt`(
IN p INT(10))
BEGIN
	SELECT COUNT(*) FROM employee e
	WHERE employee_pin = p;
END$$
DELIMITER ;
