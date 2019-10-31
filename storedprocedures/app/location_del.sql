DROP procedure IF EXISTS `location_del`;
DELIMITER $$
CREATE PROCEDURE `location_del`(
IN lid INT(7))
BEGIN
	SET @count = 0;
	
	-- Verify that the location has no party history
	SELECT COUNT(*) INTO @count FROM party p
	INNER JOIN table_restaurant tr ON p.party_table_restaurant_id = tr.table_restaurant_id
	INNER JOIN location l ON tr.table_restaurant_location_id = l.location_id
	WHERE l.location_id = lid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This location has party and order history.";
	END IF;
	
	-- Verify that the location has no positions assigned
	SELECT COUNT(*) INTO @count FROM position_role 
	WHERE position_role_location_id = lid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This location has positions assigned.";
	END IF;
	
	-- Verify that the location has no timsheet history
	SELECT COUNT(*) INTO @count FROM timesheet_day
	WHERE timesheet_day_location_id = lid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This location has timesheet history.";
	END IF;
	
	DELETE FROM location
	WHERE location_id = lid;
	
	DELETE FROM employee_authorized_location
	WHERE location_id = lid;
	
	DELETE FROM menu
	WHERE menu_location_id = lid;
	
	DELETE FROM table_restaurant
	WHERE table_restaurant_location_id = lid;
END$$
DELIMITER ;
