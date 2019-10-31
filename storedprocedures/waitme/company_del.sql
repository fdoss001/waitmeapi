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