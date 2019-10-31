DROP procedure IF EXISTS `item_upd`;
DELIMITER $$
CREATE PROCEDURE `item_upd`(
IN iid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ds TEXT,
IN bp DECIMAL(8,2),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE item SET
	item_code = co,
	item_name = na,
	item_description = ds,
	item_base_price = bp, 
	item_active = ac,
	item_lastupdate_dtm = CURRENT_TIMESTAMP,
	item_lastupdate_user_id = luuid
	WHERE item_id = iid;
END$$
DELIMITER ;
