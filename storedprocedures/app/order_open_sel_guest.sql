DROP procedure IF EXISTS `order_open_sel_guest`;
DELIMITER $$
CREATE PROCEDURE `order_open_sel_guest`(
IN gid INT(11))
BEGIN
	SELECT * FROM order_open
	WHERE order_open_guest_id = gid;
END$$
DELIMITER ;
