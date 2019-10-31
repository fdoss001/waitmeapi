DROP procedure IF EXISTS `sub_category_ins`;
DELIMITER $$
CREATE PROCEDURE `sub_category_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	INSERT INTO sub_category (sub_category_code, sub_category_name, sub_category_active, sub_category_lastupdate_user_id)
	VALUES (co, na, ac, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
