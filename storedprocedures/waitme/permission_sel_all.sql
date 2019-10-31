DROP procedure IF EXISTS `permission_sel_all`;
DELIMITER $$
CREATE PROCEDURE `permission_sel_all`()
BEGIN
	SELECT * FROM permission;
END$$
DELIMITER ;
