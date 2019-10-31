DROP procedure IF EXISTS `employee_ins`;
DELIMITER $$
CREATE PROCEDURE `employee_ins`(
IN cid INT(7),
IN un VARCHAR(45),
IN fn VARCHAR(45),
IN ln VARCHAR(45),
IN em VARCHAR(45),
IN ph VARCHAR(20),
IN ad TEXT,
IN pwd BINARY(64),
IN st BINARY(16),
IN p INT(10),
IN pid TINYINT(3),
IN pa DECIMAL(8,2),
IN luuid INT(10))
BEGIN
	SET @lastinsertid = 0;
	SET @count = 0;
	
	-- Verify that the username is globally unique
	SELECT COUNT(*) INTO @count FROM waitme.company_user WHERE company_user_uname = un;
	IF @count > 0 THEN
		SET @errortext = CONCAT("Duplicate entry '", un, "' for key 'uname_UNIQUE'");
		SIGNAL SQLSTATE '23000'
        SET MYSQL_ERRNO = 1062,
        MESSAGE_TEXT = @errortext;
	END IF;
	
	-- If it is, continue to insert in both tables
	INSERT INTO employee (employee_company_id, employee_uname, employee_fname, employee_lname, employee_email, employee_phone, employee_address, employee_passwd, employee_salt, employee_pin, employee_position_role_id, employee_pay_adjustment, employee_lastupdate_user_id)
	VALUES (cid, un, fn, ln, em, ph, ad, pwd, st, p, pid, pa,luuid);
    
	SELECT LAST_INSERT_ID() INTO @lastinsertid;
    
	INSERT INTO waitme.company_user (company_id, company_user_id, company_user_uname) VALUES (cid, @lastinsertid, un);
	
	SELECT @lastinsertid;
END$$
DELIMITER ;
