DROP procedure IF EXISTS `item_option_del`;
DELIMITER $$
CREATE PROCEDURE `item_option_del`(
IN ioid SMALLINT(4))
BEGIN
	SET @count = 0;
	
	-- Verify that the option has no order history
	SELECT COUNT(*) INTO @count FROM order_item_option
	WHERE item_option_id = ioid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This option exists in order history.";
	END IF;
	
	SET @nid = 0;
	SELECT item_option_nutrition_facts_id INTO @nid FROM item_option WHERE item_option_id = ioid;
	DELETE FROM nutrition_facts WHERE nutrition_facts_id = @nid;
	
	DELETE FROM item_option WHERE item_option_id = ioid;
	DELETE FROM item_item_option WHERE item_option_id = ioid;
END$$
DELIMITER ;
