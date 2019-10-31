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