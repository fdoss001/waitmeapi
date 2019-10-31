DROP procedure IF EXISTS `item_option_ins`;
DELIMITER $$
CREATE PROCEDURE `item_option_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ds TEXT,
IN nfid SMALLINT(4),
IN bp DECIMAL(8,2),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	INSERT INTO item_option (item_option_code, item_option_name, item_option_description, item_option_nutrition_facts_id, item_option_base_price, item_option_active, item_option_lastupdate_user_id)
	VALUES (co, na, ds, nfid, bp, ac, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
