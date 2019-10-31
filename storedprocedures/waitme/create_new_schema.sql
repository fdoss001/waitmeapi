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
