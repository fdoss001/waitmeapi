 
DROP procedure IF EXISTS `ajax_module_sub_module_sel`;
DELIMITER $$
CREATE PROCEDURE `ajax_module_sub_module_sel`(
IN sid SMALLINT(4))
BEGIN
	SELECT am.ajax_module_id, am.ajax_module_name, am.ajax_module_url FROM ajax_module am
	INNER JOIN sub_module_ajax_module smam ON smam.ajax_module_id = am.ajax_module_id
	WHERE smam.sub_module_id = sid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `company_del`;
DELIMITER $$
CREATE PROCEDURE `company_del`(
IN cid INT(7))
BEGIN
	DELETE FROM company WHERE company_id = cid;
	DELETE FROM company_settings WHERE company_id = cid;
	DELETE FROM company_user WHERE company_id = cid;
	
	DROP SCHEMA longhorn;
	DROP USER LonghornDBAdmin;
END$$
DELIMITER ;  
 
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
  
 
DROP procedure IF EXISTS `company_sel`;
DELIMITER $$
CREATE PROCEDURE `company_sel`(
IN cid INT(7))
BEGIN
	SELECT c.*, cs.* FROM company c
	INNER JOIN company_settings cs ON cs.company_id = c.company_id
	WHERE c.company_id = cid;
END$$
DELIMITER ;  
 
DROP procedure IF EXISTS `company_sel_all`;
DELIMITER $$
CREATE PROCEDURE `company_sel_all`()
BEGIN
	SELECT * FROM company;
END$$
DELIMITER ;  
 
DROP procedure IF EXISTS `company_settings_upd`;
DELIMITER $$
CREATE PROCEDURE `company_settings_upd`(
IN cid INT(10),
IN lp VARCHAR(255),
IN tp VARCHAR(255),
IN luun VARCHAR(45))
BEGIN
	UPDATE company_settings SET
	company_settings_logo_path = lp,
	company_settings_theme_path = tp,
	company_settings_lastupdate_dtm = CURRENT_TIMESTAMP,
	company_settings_lastupdate_uname = luun
    WHERE company_id = cid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `company_upd`;
DELIMITER $$
CREATE PROCEDURE `company_upd`(
IN cid INT(10),
IN na VARCHAR(45),
IN luun VARCHAR(45))
BEGIN
	UPDATE company SET
	company_name = na,
	company_lastupdate_dtm = CURRENT_TIMESTAMP,
	company_lastupdate_uname = luun
    WHERE company_id = cid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `company_user_sel`;
DELIMITER $$
CREATE PROCEDURE `company_user_sel`(
IN un VARCHAR(45))
BEGIN
	SELECT company_id FROM company_user
	WHERE company_user_uname = un;
END$$
DELIMITER ;  
 
DROP procedure IF EXISTS `company_user_sel_cnt`;
DELIMITER $$
CREATE PROCEDURE `company_user_sel_cnt`(
IN un VARCHAR(45))
BEGIN
	SELECT COUNT(*) FROM company_user
	WHERE company_user_uname = un;
END$$
DELIMITER ;  
 
DROP procedure IF EXISTS `create_new_schema`;
DELIMITER $$
CREATE PROCEDURE `create_new_schema`(
IN na VARCHAR(45))
BEGIN
	SET @sql = CONCAT(CONCAT('CREATE SCHEMA `', na), '` DEFAULT CHARACTER SET utf8mb4;');
	PREPARE stmt FROM @sql;
	EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    
    SET @user = CONCAT(CONCAT(UCASE(LEFT(na, 1)), SUBSTRING(na, 2)), 'DBAdmin');
    SET @passwd = CONCAT(CONCAT(UCASE(LEFT(na, 1)), SUBSTRING(na, 2)), '1!');
    SET @sql = CONCAT(CONCAT(CONCAT(CONCAT('CREATE USER ''', @user), '''@''%'' IDENTIFIED BY '''), @passwd), ''';');
    PREPARE stmt FROM @sql;
	EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    
	UPDATE mysql.user SET plugin='mysql_native_password' WHERE user=@user;
	
	SET @sql = CONCAT(CONCAT(CONCAT(CONCAT('GRANT ALL PRIVILEGES ON ', na), '.* TO '''), @user), '''@''%'';');
	PREPARE stmt FROM @sql;
	EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    
	SET @sql = CONCAT(CONCAT(CONCAT(CONCAT(CONCAT('GRANT SELECT ON waitme.* TO '''), @user), '''@''%'' IDENTIFIED BY '''), @passwd), ''';');
	PREPARE stmt FROM @sql;
	EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    
    SET @sql = CONCAT(CONCAT(CONCAT(CONCAT(CONCAT('GRANT DELETE, UPDATE, INSERT ON waitme.company_user TO '''), @user), '''@''%'' IDENTIFIED BY '''), @passwd), ''';');
    PREPARE stmt FROM @sql;
	EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    
	FLUSH PRIVILEGES;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `module_sel_all`;
DELIMITER $$
CREATE PROCEDURE `module_sel_all`()
BEGIN
	SELECT * FROM module;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `permission_sel_all`;
DELIMITER $$
CREATE PROCEDURE `permission_sel_all`()
BEGIN
	SELECT * FROM permission;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `sub_module_module_sel`;
DELIMITER $$
CREATE PROCEDURE `sub_module_module_sel`(
IN mid SMALLINT(4))
BEGIN
	SELECT * FROM sub_module WHERE sub_module_module_id = mid;
END$$
DELIMITER ;
  
 
