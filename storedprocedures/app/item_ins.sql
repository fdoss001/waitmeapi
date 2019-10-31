DROP procedure IF EXISTS `item_ins`;
DELIMITER $$
CREATE PROCEDURE `item_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ds TEXT,
IN nfid SMALLINT(4),
IN bp DECIMAL(8,2),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	INSERT INTO item (item_code, item_name, item_description, item_nutrition_facts_id, item_base_price, item_active, item_lastupdate_user_id)
	VALUES (co, na, ds, nfid, bp, ac, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
