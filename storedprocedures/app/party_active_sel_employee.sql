DROP procedure IF EXISTS `party_active_sel_employee`;
DELIMITER $$
CREATE PROCEDURE `party_active_sel_employee`(
IN eid INT(10)
)
BEGIN
	SELECT * FROM party WHERE party_active = TRUE
	AND party_employee_id = eid;
END$$
DELIMITER ;
