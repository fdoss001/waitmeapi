DROP procedure IF EXISTS `order_open_ins`;
DELIMITER $$
CREATE PROCEDURE `order_open_ins`(
IN gid INT(11),
IN luuid INT(10))
BEGIN	
	-- insert the new guest
	INSERT INTO order_open (order_open_guest_id, order_open_lastupdate_user_id)
	VALUES (gid, luuid);
	
	SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
