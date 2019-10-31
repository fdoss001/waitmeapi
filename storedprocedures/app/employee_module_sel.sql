DROP procedure IF EXISTS `employee_module_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_module_sel`(
IN eid INT(10))
BEGIN
	SELECT distinct(m.module_id), m.module_name FROM waitme.sub_module sm
	INNER JOIN employee_sub_module esm ON esm.sub_module_id = sm.sub_module_id
	INNER JOIN waitme.module m ON m.module_id = sm.sub_module_module_id
	WHERE esm.employee_id = eid;
END$$
DELIMITER ;
