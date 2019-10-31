DROP procedure IF EXISTS `item_del`;
DELIMITER $$
CREATE PROCEDURE `item_del`(
IN iid SMALLINT(4))
BEGIN
	SET @count = 0;
	
	-- Verify that the item has no order history
	SELECT COUNT(*) INTO @count FROM order_item
	WHERE item_id = iid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This item exists in order history.";
	END IF;
	
	SET @nid = 0;
	SELECT item_nutrition_facts_id INTO @nid FROM item WHERE item_id = iid;
	DELETE FROM nutrition_facts WHERE nutrition_facts_id = @nid;
	
	DELETE FROM item WHERE item_id = iid;
	DELETE FROM meal_item WHERE item_id = iid;
	DELETE FROM meal_item_substitute WHERE item_id = iid;
	DELETE FROM item_item_option WHERE item_id = iid;
END$$
DELIMITER ;
