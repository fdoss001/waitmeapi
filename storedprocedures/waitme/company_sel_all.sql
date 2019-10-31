DROP procedure IF EXISTS `company_sel_all`;
DELIMITER $$
CREATE PROCEDURE `company_sel_all`()
BEGIN
	SELECT * FROM company;
END$$
DELIMITER ;