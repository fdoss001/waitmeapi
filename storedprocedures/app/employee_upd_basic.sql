DROP procedure IF EXISTS `employee_upd_basic`;
DELIMITER $$
CREATE PROCEDURE `employee_upd_basic`(
IN uid INT(10),
IN un VARCHAR(45),
IN fn VARCHAR(45),
IN ln VARCHAR(45),
IN em VARCHAR(45),
IN ph VARCHAR(20),
IN addr TEXT,
IN uuid INT(10))
BEGIN
	SET @count = 0;
	SET @old_uname = '';
	
	-- verify that this user is not updating their username
	SELECT employee_uname INTO @old_uname FROM employee WHERE employee_id = uid;
	
	IF @old_uname != un THEN
		-- Verify that the username is globally unique
		SELECT COUNT(*) INTO @count FROM waitme.company_user WHERE company_user_uname = un;
		IF @count > 0 THEN
			SET @errortext = CONCAT("Duplicate entry '", un, "' for key 'uname_UNIQUE'");
			SIGNAL SQLSTATE '23000'
	        SET MYSQL_ERRNO = 1062,
	        MESSAGE_TEXT = @errortext;
		END IF;
	END IF;
	
	UPDATE employee SET
	employee_uname = un,
    employee_fname = fn,
    employee_lname = ln,
    employee_email = em,
    employee_phone = ph,
    employee_address = addr,
    employee_lastupdate_dtm = CURRENT_TIMESTAMP,
    employee_lastupdate_user_id = uuid
    WHERE employee_id = uid;
    
    UPDATE waitme.company_user SET
    company_user_uname = un,
    company_user_lastupdate_dtm = CURRENT_TIMESTAMP
    WHERE company_user_uname = @old_uname;
END$$
DELIMITER ;
