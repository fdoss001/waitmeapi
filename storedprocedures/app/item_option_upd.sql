DROP procedure IF EXISTS `item_option_upd`;
DELIMITER $$
CREATE PROCEDURE `item_option_upd`(
IN ioid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ds TEXT,
IN bp DECIMAL(8,2),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE item_option SET
	item_option_code = co,
	item_option_name = na,
	item_option_description = ds,
	item_option_base_price = bp,
	item_option_active = ac,
	item_option_lastupdate_dtm = CURRENT_TIMESTAMP,
	item_option_lastupdate_user_id = luuid
	WHERE item_option_id = ioid;
END$$
DELIMITER ;
