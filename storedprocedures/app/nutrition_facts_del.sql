DROP procedure IF EXISTS `nutrition_facts_del`;
DELIMITER $$
CREATE PROCEDURE `nutrition_facts_del`(
IN nfid SMALLINT(4))
BEGIN
	DELETE FROM nutrition_facts WHERE nutrition_facts_id = nfid;
END$$
DELIMITER ;
