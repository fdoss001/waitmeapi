DROP procedure IF EXISTS `employee_sel_pwd`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_pwd`(
IN uid INT(10))
BEGIN
	SELECT employee_passwd, employee_salt FROM employee
	WHERE employee_id = uid;
END$$
DELIMITER ;
