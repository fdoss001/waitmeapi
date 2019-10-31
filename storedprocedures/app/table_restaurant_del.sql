DROP procedure IF EXISTS `table_restaurant_del`;
DELIMITER $$
CREATE PROCEDURE `table_restaurant_del`(
IN tid INT(10))
BEGIN
	SET @count = 0;
	
	-- Verify that the table has no party history
	SELECT COUNT(*) INTO @count FROM party
	WHERE party_table_restaurant_id = tid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This table has party and order history.";
	END IF;
	
	DELETE FROM table_restaurant
	WHERE table_restaurant_id = tid;
END$$
DELIMITER ;
