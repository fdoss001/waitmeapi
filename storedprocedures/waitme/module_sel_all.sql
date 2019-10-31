DROP procedure IF EXISTS `module_sel_all`;
DELIMITER $$
CREATE PROCEDURE `module_sel_all`()
BEGIN
	SELECT * FROM module;
END$$
DELIMITER ;
