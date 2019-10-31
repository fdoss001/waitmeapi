DROP procedure IF EXISTS `order_item_ins`;
DELIMITER $$
CREATE PROCEDURE `order_item_ins`(
IN oid INT(11),
IN iid SMALLINT(4))
BEGIN
	INSERT INTO order_item (order_id, item_id)
	VALUES (oid, iid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
