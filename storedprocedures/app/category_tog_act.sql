DROP procedure IF EXISTS `category_tog_act`;
DELIMITER $$
CREATE PROCEDURE `category_tog_act`(
IN cid SMALLINT(4),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE category SET
	category_active = ac,
	category_lastupdate_dtm = CURRENT_TIMESTAMP,
	category_lastupdate_user_id = luuid
	WHERE category_id = cid;
END$$
DELIMITER ;