DROP procedure IF EXISTS `nutrition_facts_ins`;
DELIMITER $$
CREATE PROCEDURE `nutrition_facts_ins`(
IN pserving_size VARCHAR(45),
IN pservings SMALLINT(4),
IN pcalories SMALLINT(4),
IN pfat_calories SMALLINT(4),
IN ptotal_fat SMALLINT(4),
IN psaturated_fat SMALLINT(4),
IN ptrans_fat SMALLINT(4),
IN pcholesterol SMALLINT(8),
IN psodium SMALLINT(8),
IN ppotassium SMALLINT(8),
IN ptotal_carb SMALLINT(4),
IN pdietary_fiber SMALLINT(4),
IN psugars SMALLINT(4),
IN psugar_alcohols SMALLINT(4),
IN pprotein SMALLINT(4),
IN pvitaminA SMALLINT(3),
IN pvitaminB6 SMALLINT(3),
IN pvitaminB12 SMALLINT(3),
IN pvitaminC SMALLINT(3),
IN pvitaminE SMALLINT(3),
IN pvitaminK SMALLINT(3),
IN pfolate SMALLINT(3),
IN pmagnesium SMALLINT(3),
IN pthiamin SMALLINT(3),
IN pzinc SMALLINT(3),
IN pcalcium SMALLINT(3),
IN priboflavin SMALLINT(3),
IN pbiotin SMALLINT(3),
IN piron SMALLINT(3),
IN pniacin SMALLINT(3),
IN ppantothenic_acid SMALLINT(3),
IN pphosphorus SMALLINT(3))
BEGIN
	INSERT INTO nutrition_facts (nutrition_facts_serving_size, nutrition_facts_servings, nutrition_facts_calories, nutrition_facts_fat_calories, nutrition_facts_total_fat, nutrition_facts_saturated_fat, nutrition_facts_trans_fat, nutrition_facts_cholesterol, nutrition_facts_sodium, nutrition_facts_potassium, nutrition_facts_total_carb, nutrition_facts_dietary_fiber, nutrition_facts_sugars, nutrition_facts_sugar_alcohols, nutrition_facts_protein, nutrition_facts_vitaminA, nutrition_facts_vitaminB6, nutrition_facts_vitaminB12, nutrition_facts_vitaminC, nutrition_facts_vitaminE, nutrition_facts_vitaminK, nutrition_facts_folate, nutrition_facts_magnesium, nutrition_facts_thiamin, nutrition_facts_zinc, nutrition_facts_calcium, nutrition_facts_riboflavin, nutrition_facts_biotin, nutrition_facts_iron, nutrition_facts_niacin, nutrition_facts_pantothenic_acid, nutrition_facts_phosphorus)
	VALUES (pserving_size, pservings, pcalories, pfat_calories, ptotal_fat, psaturated_fat, ptrans_fat, pcholesterol, psodium, ppotassium, ptotal_carb, pdietary_fiber, psugars, psugar_alcohols, pprotein, pvitaminA, pvitaminB6, pvitaminB12, pvitaminC, pvitaminE, pvitaminK, pfolate, pmagnesium, pthiamin, pzinc, pcalcium, priboflavin, pbiotin, piron, pniacin, ppantothenic_acid, pphosphorus);
	SELECT LAST_INSERT_ID();	
END$$
DELIMITER ;
