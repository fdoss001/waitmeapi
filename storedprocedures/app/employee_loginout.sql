DROP procedure IF EXISTS `employee_loginout`;
DELIMITER $$
CREATE PROCEDURE `employee_loginout`(
IN uid INT(10),
IN li TINYINT(1))
BEGIN
	UPDATE employee SET employee_loggedin = li 
    WHERE employee_id = uid;
    IF li != 0 THEN
		UPDATE employee SET employee_lastlogin_dtm = CURRENT_TIMESTAMP
        WHERE employee_id = uid;
	END IF;
END$$
DELIMITER ;
