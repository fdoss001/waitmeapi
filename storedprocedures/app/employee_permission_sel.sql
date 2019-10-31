DROP procedure IF EXISTS `employee_permission_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_permission_sel`(
IN uid INT(10))
BEGIN
	SELECT * FROM waitme.permission p
	INNER JOIN employee_permission ep ON ep.permission_id = p.permission_id
    WHERE ep.employee_id = uid;
END$$
DELIMITER ;
