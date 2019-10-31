DROP procedure IF EXISTS `company_ins`;
DELIMITER $$
CREATE PROCEDURE `company_ins`(
IN na VARCHAR(45),
IN luun VARCHAR(45))
BEGIN
	
	-- Verify that the company name is unique
	SELECT COUNT(*) INTO @count FROM company WHERE UPPER(REPLACE(company_name, ' ', '')) = UPPER(REPLACE(na, ' ', ''));
	IF @count > 0 THEN
		SET @errortext = CONCAT("Duplicate entry '", na, "' for key 'uname_UNIQUE'");
		SIGNAL SQLSTATE '23000'
        SET MYSQL_ERRNO = 1062,
        MESSAGE_TEXT = @errortext;
	END IF;
	
	INSERT INTO company (company_name, company_dbname, company_lastupdate_uname)
	VALUES (na, LOWER(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(company_name, ' ', ''), "'", ''), ',', ''), '&', ''), '!', '')), luun);
	
	SELECT LAST_INSERT_ID() INTO @lastinsertid;
	
	INSERT INTO company_settings(company_id, company_settings_lastupdate_uname)
	VALUES (@lastinsertid, luun);
	
	SELECT @lastinsertid;
END$$
DELIMITER ;
