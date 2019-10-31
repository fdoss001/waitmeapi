DROP procedure IF EXISTS `party_ins`;
DELIMITER $$
CREATE PROCEDURE `party_ins`(
IN tid INT(10),
IN eid INT(10),
IN luuid INT(10))
BEGIN
	-- Verify that an active party is not in the new table
	SELECT COUNT(*) INTO @count FROM party WHERE party_table_restaurant_id = tid AND party_active = TRUE;
	IF @count > 0 THEN
		SET @errortext = CONCAT("Duplicate entry '", tid, "' for key 'table_restaurant_id_UNIQUE'");
		SIGNAL SQLSTATE '23000'
		SET MYSQL_ERRNO = 1062,
		MESSAGE_TEXT = @errortext;
	END IF;
	
	-- insert the new party
	INSERT INTO party (party_table_restaurant_id, party_employee_id, party_lastupdate_user_id)
	VALUES (tid, eid, luuid);
    
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;