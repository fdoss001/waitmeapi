DROP procedure IF EXISTS `employee_permission_ins`;
DELIMITER $$
CREATE PROCEDURE `employee_permission_ins`(
IN uid INT(10),
IN pid SMALLINT(4))
BEGIN
	INSERT INTO employee_permission (employee_id, permission_id)
    VALUES (uid, pid);
END$$
DELIMITER ;
