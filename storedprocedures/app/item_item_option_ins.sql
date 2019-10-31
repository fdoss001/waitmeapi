DROP procedure IF EXISTS `item_item_option_ins`;
DELIMITER $$
CREATE PROCEDURE `item_item_option_ins`(
IN iid SMALLINT(4),
IN ioid SMALLINT(4),
IN pa DECIMAL(8,2),
IN qt ENUM('light', 'normal', 'extra'))
BEGIN
	IF pa IS NULL AND qt is NULL THEN
		INSERT INTO item_item_option (item_id, item_option_id)
		VALUES (iid, ioid);
	ELSEIF pa is NULL THEN
		INSERT INTO item_item_option (item_id, item_option_id, item_item_option_quantity)
		VALUES (iid, ioid, qt);
	ELSEIF qt is NULL THEN
		INSERT INTO item_item_option (item_id, item_option_id, item_item_option_price_adjustment)
		VALUES (iid, ioid, pa);
	ELSE
		INSERT INTO item_item_option (item_id, item_option_id, item_item_option_price_adjustment, item_item_option_quantity)
		VALUES (iid, ioid, pa, qt);
	END IF;
END$$
DELIMITER ;
