DROP procedure IF EXISTS `order_item_option_ins`;
DELIMITER $$
CREATE PROCEDURE `order_item_option_ins`(
IN oiid INT(11),
IN ioid SMALLINT(4))
BEGIN
	INSERT INTO order_item_option (order_item_id, item_option_id)
	VALUES (oiid, ioid);
	
	SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
