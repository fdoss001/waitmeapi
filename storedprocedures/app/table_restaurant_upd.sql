DROP procedure IF EXISTS `table_restaurant_upd`;
DELIMITER $$
CREATE PROCEDURE `table_restaurant_upd`(
IN tid INT(10),
IN na VARCHAR(45),
IN lid INT(7),
IN px SMALLINT(4),
IN py SMALLINT(4),
IN sz VARCHAR(10),
IN sh ENUM('circle', 'rectangle'),
IN cp TINYINT(2),
IN ac TINYINT(1),
IN luid INT(10))
BEGIN
	-- Verify that the table name is unique for the location
	SELECT COUNT(*) INTO @count FROM table_restaurant 
	WHERE table_restaurant_name = na AND table_restaurant_location_id = lid AND table_restaurant_id <> tid;
	IF @count > 0 THEN
		SET @errortext = CONCAT("Duplicate entry '", na, ", ", lid, "' for key 'table_restaurant_name_UNIQUE' and 'table_restaurant_location_id_UNIQUE'");
		SIGNAL SQLSTATE '23000'
        SET MYSQL_ERRNO = 1062,
        MESSAGE_TEXT = @errortext;
	END IF;	
	
	UPDATE table_restaurant SET
	table_restaurant_name = na,
	table_restaurant_location_id = lid,
	table_restaurant_posx = px,
	table_restaurant_posy = py,
	table_restaurant_size = sz,
	table_restaurant_shape = sh,
	table_restaurant_capacity = cp,
	table_restaurant_active = ac,
	table_restaurant_lastupdate_user_id = luid,
	table_restaurant_lastupdate_dtm = CURRENT_TIMESTAMP
	WHERE table_restaurant_id = tid;
END$$
DELIMITER ;
