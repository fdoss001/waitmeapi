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
