DROP procedure IF EXISTS `nutrition_facts_upd`;
DELIMITER $$
CREATE PROCEDURE `nutrition_facts_upd`(
IN nfid SMALLINT(4),
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
	UPDATE nutrition_facts SET
	nutrition_facts_serving_size = pserving_size,
	nutrition_facts_servings = pservings,
	nutrition_facts_calories = pcalories,
	nutrition_facts_fat_calories = pfat_calories,
	nutrition_facts_total_fat = ptotal_fat,
	nutrition_facts_saturated_fat = psaturated_fat,
	nutrition_facts_trans_fat = ptrans_fat,
	nutrition_facts_cholesterol = pcholesterol,
	nutrition_facts_sodium = psodium,
	nutrition_facts_potassium = ppotassium,
	nutrition_facts_total_carb = ptotal_carb,
	nutrition_facts_dietary_fiber = pdietary_fiber,
	nutrition_facts_sugars = psugars,
	nutrition_facts_sugar_alcohols = psugar_alcohols,
	nutrition_facts_protein = pprotein,
	nutrition_facts_vitaminA = pvitaminA,
	nutrition_facts_vitaminB6 = pvitaminB6,
	nutrition_facts_vitaminB12 = pvitaminB12,
	nutrition_facts_vitaminC = pvitaminC,
	nutrition_facts_vitaminE = pvitaminE,
	nutrition_facts_vitaminK = pvitaminK,
	nutrition_facts_folate = pfolate,
	nutrition_facts_magnesium = pmagnesium,
	nutrition_facts_thiamin = pthiamin,
	nutrition_facts_zinc = pzinc,
	nutrition_facts_calcium = pcalcium,
	nutrition_facts_riboflavin = priboflavin,
	nutrition_facts_biotin = pbiotin,
	nutrition_facts_iron = piron,
	nutrition_facts_niacin = pniacin,
	nutrition_facts_pantothenic_acid = ppantothenic_acid,
	nutrition_facts_phosphorus = pphosphorus
	WHERE nutrition_facts_id = nfid;
END$$
DELIMITER ;
