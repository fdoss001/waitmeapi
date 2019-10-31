DROP procedure IF EXISTS `guest_sel`;
DELIMITER $$
CREATE PROCEDURE `guest_sel`(
IN gid INT(11)
)
BEGIN
	SELECT * FROM guest
	WHERE guest_id = gid;
END$$
DELIMITER ;