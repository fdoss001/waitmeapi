DROP procedure IF EXISTS `employee_sub_module_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_sub_module_sel`(
IN eid INT(10),
IN mid SMALLINT(4))
BEGIN
	SELECT sm.*, esm.* FROM waitme.sub_module sm
	INNER JOIN employee_sub_module esm ON esm.sub_module_id = sm.sub_module_id
	WHERE esm.employee_id = eid AND sm.sub_module_module_id = mid;
END$$
DELIMITER ;
