DROP procedure IF EXISTS `table_restaurant_ins`;
DELIMITER $$
CREATE PROCEDURE `table_restaurant_ins`(
IN na VARCHAR(45),
IN lid INT(7),
IN px SMALLINT(4),
IN py SMALLINT(4),
IN sz VARCHAR(10),
IN sh ENUM('circle', 'rectangle'),
IN cp TINYINT(2),
IN luid INT(10))
BEGIN
	-- Verify that the table name is unique for the location
	SELECT COUNT(*) INTO @count FROM table_restaurant WHERE table_restaurant_name = na AND table_restaurant_location_id = lid;
	IF @count > 0 THEN
		SET @errortext = CONCAT("Duplicate entry '", na, ", ", lid, "' for key 'table_restaurant_name_UNIQUE' and 'table_restaurant_location_id_UNIQUE'");
		SIGNAL SQLSTATE '23000'
        SET MYSQL_ERRNO = 1062,
        MESSAGE_TEXT = @errortext;
	END IF;	
	
	INSERT INTO table_restaurant (table_restaurant_name, table_restaurant_location_id, table_restaurant_posx, table_restaurant_posy, table_restaurant_size, table_restaurant_shape, table_restaurant_capacity, table_restaurant_lastupdate_user_id)
	VALUES (na, lid, px, py, sz, sh, cp, luid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
