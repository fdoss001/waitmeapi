DROP procedure IF EXISTS `guest_sel_party`;
DELIMITER $$
CREATE PROCEDURE `guest_sel_party`(
IN pid INT(11)
)
BEGIN
	SELECT * FROM guest
	WHERE guest_party_id = pid
	AND guest_active = TRUE;
END$$
DELIMITER ;