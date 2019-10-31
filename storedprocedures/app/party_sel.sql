DROP procedure IF EXISTS `party_sel`;
DELIMITER $$
CREATE PROCEDURE `party_sel`(
IN pid INT(11)
)
BEGIN
	SELECT * FROM party WHERE party_id = pid;
END$$
DELIMITER ;
