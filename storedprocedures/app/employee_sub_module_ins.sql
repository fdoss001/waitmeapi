DROP procedure IF EXISTS `employee_sub_module_ins`;
DELIMITER $$
CREATE PROCEDURE `employee_sub_module_ins`(
IN eid INT(10),
IN smid SMALLINT(4),
IN s TINYINT(1),
IN i TINYINT(1),
IN u TINYINT(1),
IN d TINYINT(1))
BEGIN
	INSERT INTO employee_sub_module (employee_id, sub_module_id, employee_sub_module_sel, employee_sub_module_ins, employee_sub_module_upd, employee_sub_module_del)
	VALUES (eid, smid, s, i, u ,d);
END$$
DELIMITER ;
