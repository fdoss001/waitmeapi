DROP procedure IF EXISTS `item_option_sel_next_id`;
DELIMITER $$
CREATE PROCEDURE `item_option_sel_next_id`()
BEGIN
	SELECT MAX(item_option_id) + 1 FROM item_option;
END$$
DELIMITER ;
