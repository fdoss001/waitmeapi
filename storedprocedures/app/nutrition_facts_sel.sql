DROP procedure IF EXISTS `nutrition_facts_sel`;
DELIMITER $$
CREATE PROCEDURE `nutrition_facts_sel`(
IN nfid SMALLINT(4))
BEGIN
	SELECT * FROM nutrition_facts
	WHERE nutrition_facts_id = nfid;
END$$
DELIMITER ;
