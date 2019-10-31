DROP procedure IF EXISTS `employee_permission_del`;
DELIMITER $$
CREATE PROCEDURE `employee_permission_del`(
IN uid INT(10),
IN pid SMALLINT(4))
BEGIN
	DELETE FROM employee_permission
    WHERE permission_id = pid AND employee_id = uid;
END$$
DELIMITER ;
