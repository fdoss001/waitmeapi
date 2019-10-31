DROP procedure IF EXISTS `employee_upd_pwd`;
DELIMITER $$
CREATE PROCEDURE `employee_upd_pwd`(
IN uid INT(10),
IN pwd BINARY(64),
IN st BINARY(16))
BEGIN
	UPDATE employee SET
    employee_passwd = pwd,
    employee_salt = st,
    employee_lastupdate_dtm = CURRENT_TIMESTAMP
    WHERE employee_id = uid;
END$$
DELIMITER ;
