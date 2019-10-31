DROP procedure IF EXISTS `guest_ins`;
DELIMITER $$
CREATE PROCEDURE `guest_ins`(
IN wuid INT(11),
IN pid INT(11),
IN fn VARCHAR(45),
IN ln VARCHAR(45))
BEGIN
	-- Verify that the username is globally unique
    IF wuid > 0 THEN
		SELECT COUNT(*) INTO @count FROM guest WHERE guest_waitme_user_id = wuid;
		IF @count > 0 THEN
			SET @errortext = CONCAT("Duplicate entry '", wuid, "' for key 'waitme_userid_UNIQUE'");
			SIGNAL SQLSTATE '23000'
			SET MYSQL_ERRNO = 1062,
			MESSAGE_TEXT = @errortext;
		END IF;
	END IF;
	
	
	-- insert the new guest
	INSERT INTO guest (guest_waitme_user_id, guest_party_id, guest_fname, guest_lname)
	VALUES (wuid, pid, fn, ln);
	
	SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
