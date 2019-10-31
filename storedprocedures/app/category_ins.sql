DROP procedure IF EXISTS `category_ins`;
DELIMITER $$
CREATE PROCEDURE `category_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	INSERT INTO category (category_code, category_name, category_active, category_lastupdate_user_id)
	VALUES (co, na, ac, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
