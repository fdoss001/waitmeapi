DROP procedure IF EXISTS `company_user_sel`;
DELIMITER $$
CREATE PROCEDURE `company_user_sel`(
IN un VARCHAR(45))
BEGIN
	SELECT company_id FROM company_user
	WHERE company_user_uname = un;
END$$
DELIMITER ;