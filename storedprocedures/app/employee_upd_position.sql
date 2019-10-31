DROP procedure IF EXISTS `employee_upd_position`;
DELIMITER $$
CREATE PROCEDURE `employee_upd_position`(
IN uid INT(10),
IN pid TINYINT(3),
IN paj DECIMAL(8,2),
IN uuid INT(10))
BEGIN	
	UPDATE employee SET
	employee_position_role_id = pid,
	employee_pay_adjustment = paj,
    employee_lastupdate_dtm = CURRENT_TIMESTAMP,
    employee_lastupdate_user_id = uuid
    WHERE employee_id = uid;
END$$
DELIMITER ;
