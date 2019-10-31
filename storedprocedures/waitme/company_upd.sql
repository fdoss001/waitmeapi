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
