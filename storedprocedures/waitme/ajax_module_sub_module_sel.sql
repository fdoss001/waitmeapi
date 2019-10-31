DROP procedure IF EXISTS `ajax_module_sub_module_sel`;
DELIMITER $$
CREATE PROCEDURE `ajax_module_sub_module_sel`(
IN sid SMALLINT(4))
BEGIN
	SELECT am.ajax_module_id, am.ajax_module_name, am.ajax_module_url FROM ajax_module am
	INNER JOIN sub_module_ajax_module smam ON smam.ajax_module_id = am.ajax_module_id
	WHERE smam.sub_module_id = sid;
END$$
DELIMITER ;
