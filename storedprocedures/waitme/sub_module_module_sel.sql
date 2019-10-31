DROP procedure IF EXISTS `sub_module_module_sel`;
DELIMITER $$
CREATE PROCEDURE `sub_module_module_sel`(
IN mid SMALLINT(4))
BEGIN
	SELECT * FROM sub_module WHERE sub_module_module_id = mid;
END$$
DELIMITER ;
