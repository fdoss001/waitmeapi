DROP procedure IF EXISTS `party_upd`;
DELIMITER $$
CREATE PROCEDURE `party_upd`(
IN pid INT(11),
IN tid VARCHAR(45),
IN eid VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	-- Verify that an active party is not in the new table
	SELECT party_table_restaurant_id INTO @oldTid FROM party WHERE party_id = pid;
    IF @oldTid <> tid THEN
		SELECT COUNT(*) INTO @count FROM party WHERE party_table_restaurant_id = tid AND party_active = TRUE;
		IF @count > 0 THEN
			SET @errortext = CONCAT("Duplicate entry '", tid, "' for key 'table_restaurant_id_UNIQUE'");
			SIGNAL SQLSTATE '23000'
			SET MYSQL_ERRNO = 1062,
			MESSAGE_TEXT = @errortext;
		END IF;
	END IF;
	
	UPDATE party SET
	party_table_restaurant_id = tid,
	party_employee_id = eid,
	party_active = ac,
	party_lastupdate_dtm = CURRENT_TIMESTAMP,
	party_lastupdate_user_id = luuid
	WHERE party_id = pid;
END$$
DELIMITER ;
