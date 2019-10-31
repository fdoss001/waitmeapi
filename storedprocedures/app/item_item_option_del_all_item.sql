DROP procedure IF EXISTS `item_item_option_del_all_item`;
DELIMITER $$
CREATE PROCEDURE `item_item_option_del_all_item`(
IN iid SMALLINT(4))
BEGIN
	DELETE FROM item_item_option WHERE item_id = iid;
END$$
DELIMITER ;
