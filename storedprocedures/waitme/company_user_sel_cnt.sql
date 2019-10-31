DROP procedure IF EXISTS `company_user_sel_cnt`;
DELIMITER $$
CREATE PROCEDURE `company_user_sel_cnt`(
IN un VARCHAR(45))
BEGIN
	SELECT COUNT(*) FROM company_user
	WHERE company_user_uname = un;
END$$
DELIMITER ;