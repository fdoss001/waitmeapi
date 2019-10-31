DROP procedure IF EXISTS `position_del`;
DELIMITER $$
CREATE PROCEDURE `position_del`(
IN pid TINYINT(3))
BEGIN
	SET @count = 0;
	
	-- Verify that the employee has no party history
	SELECT COUNT(*) INTO @count FROM employee
	WHERE employee_position_role_id = pid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This position has employees assigned to it.";
	END IF;

	DELETE FROM position_role
	WHERE position_role_id = pid;
END$$
DELIMITER ;
