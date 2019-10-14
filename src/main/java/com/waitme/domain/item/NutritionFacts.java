package com.waitme.domain.item;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waitme.domain.WMDomainObject;

/**
 * Class to represent a set of nutrition facts for a food item
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-14
 * @since 1.0 2019-02-14
 */
public class NutritionFacts extends WMDomainObject implements RowMapper<NutritionFacts> {
	private int id;
	private int servings, calories, fat_calories, total_fat, saturated_fat, trans_fat, cholesterol, sodium, potassium, total_carb, dietary_fiber, sugars, sugar_alcohols, protein, vitaminA, vitaminB6, vitaminB12, vitaminC, vitaminE, vitaminK, folate, magnesium, thiamin, zinc, calcium, riboflavin, biotin, iron, niacin, pantothenic_acid, phosphorus;
	String serving_size;

	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(NutritionFacts.class);
	
	public NutritionFacts() {}
	
	public NutritionFacts(int id, int calories) {
		this.id = id;
		this.calories = calories;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}

	public int getFat_calories() {
		return fat_calories;
	}

	public void setFat_calories(int fat_calories) {
		this.fat_calories = fat_calories;
	}

	public int getTotal_fat() {
		return total_fat;
	}

	public void setTotal_fat(int total_fat) {
		this.total_fat = total_fat;
	}

	public int getSaturated_fat() {
		return saturated_fat;
	}

	public void setSaturated_fat(int saturated_fat) {
		this.saturated_fat = saturated_fat;
	}

	public int getTrans_fat() {
		return trans_fat;
	}

	public void setTrans_fat(int trans_fat) {
		this.trans_fat = trans_fat;
	}

	public int getCholesterol() {
		return cholesterol;
	}

	public void setCholesterol(int cholesterol) {
		this.cholesterol = cholesterol;
	}

	public int getSodium() {
		return sodium;
	}

	public void setSodium(int sodium) {
		this.sodium = sodium;
	}

	public int getPotassium() {
		return potassium;
	}

	public void setPotassium(int potassium) {
		this.potassium = potassium;
	}

	public int getTotal_carb() {
		return total_carb;
	}

	public void setTotal_carb(int total_carb) {
		this.total_carb = total_carb;
	}

	public int getDietary_fiber() {
		return dietary_fiber;
	}

	public void setDietary_fiber(int dietary_fiber) {
		this.dietary_fiber = dietary_fiber;
	}

	public int getSugars() {
		return sugars;
	}

	public void setSugars(int sugars) {
		this.sugars = sugars;
	}

	public int getSugar_alcohols() {
		return sugar_alcohols;
	}

	public void setSugar_alcohols(int sugar_alcohols) {
		this.sugar_alcohols = sugar_alcohols;
	}

	public int getProtein() {
		return protein;
	}

	public void setProtein(int protein) {
		this.protein = protein;
	}

	public int getVitaminA() {
		return vitaminA;
	}

	public void setVitaminA(int vitaminA) {
		this.vitaminA = vitaminA;
	}

	public int getVitaminB6() {
		return vitaminB6;
	}

	public void setVitaminB6(int vitaminB6) {
		this.vitaminB6 = vitaminB6;
	}

	public int getVitaminB12() {
		return vitaminB12;
	}

	public void setVitaminB12(int vitaminB12) {
		this.vitaminB12 = vitaminB12;
	}

	public int getVitaminC() {
		return vitaminC;
	}

	public void setVitaminC(int vitaminC) {
		this.vitaminC = vitaminC;
	}

	public int getVitaminE() {
		return vitaminE;
	}

	public void setVitaminE(int vitaminE) {
		this.vitaminE = vitaminE;
	}

	public int getVitaminK() {
		return vitaminK;
	}

	public void setVitaminK(int vitaminK) {
		this.vitaminK = vitaminK;
	}

	public int getFolate() {
		return folate;
	}

	public void setFolate(int folate) {
		this.folate = folate;
	}

	public int getMagnesium() {
		return magnesium;
	}

	public void setMagnesium(int magnesium) {
		this.magnesium = magnesium;
	}

	public int getThiamin() {
		return thiamin;
	}

	public void setThiamin(int thiamin) {
		this.thiamin = thiamin;
	}

	public int getZinc() {
		return zinc;
	}

	public void setZinc(int zinc) {
		this.zinc = zinc;
	}

	public int getCalcium() {
		return calcium;
	}

	public void setCalcium(int calcium) {
		this.calcium = calcium;
	}

	public int getRiboflavin() {
		return riboflavin;
	}

	public void setRiboflavin(int riboflavin) {
		this.riboflavin = riboflavin;
	}

	public int getBiotin() {
		return biotin;
	}

	public void setBiotin(int biotin) {
		this.biotin = biotin;
	}

	public int getIron() {
		return iron;
	}

	public void setIron(int iron) {
		this.iron = iron;
	}

	public int getNiacin() {
		return niacin;
	}

	public void setNiacin(int niacin) {
		this.niacin = niacin;
	}

	public int getPantothenic_acid() {
		return pantothenic_acid;
	}

	public void setPantothenic_acid(int pantothenic_acid) {
		this.pantothenic_acid = pantothenic_acid;
	}

	public int getPhosphorus() {
		return phosphorus;
	}

	public void setPhosphorus(int phosphorus) {
		this.phosphorus = phosphorus;
	}

	public String getServing_size() {
		return serving_size;
	}

	public void setServing_size(String serving_size) {
		this.serving_size = serving_size;
	}
	
	@Override
	public NutritionFacts mapRow(ResultSet rs, int rowNum) throws SQLException {
		NutritionFacts nutritionFacts = new NutritionFacts();
		
		try {nutritionFacts.setId(rs.getInt("nutrition_facts_id"));} catch(SQLException e) {throw new SQLException("No id for nutrition facts. Not found.");}
		try {nutritionFacts.setServing_size(rs.getString("nutrition_facts_serving_size"));} catch(SQLException e) {log.debug("No code for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setServings(rs.getInt("nutrition_facts_servings"));} catch(SQLException e) {log.debug("No servings for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setCalories(rs.getInt("nutrition_facts_calories"));} catch(SQLException e) {log.debug("No calories for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setFat_calories(rs.getInt("nutrition_facts_fat_calories"));} catch(SQLException e) {log.debug("No fat_calories for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setTotal_fat(rs.getInt("nutrition_facts_total_fat"));} catch(SQLException e) {log.debug("No total_fat for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setSaturated_fat(rs.getInt("nutrition_facts_saturated_fat"));} catch(SQLException e) {log.debug("No saturated_fat for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setTrans_fat(rs.getInt("nutrition_facts_trans_fat"));} catch(SQLException e) {log.debug("No trans_fat for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setCholesterol(rs.getInt("nutrition_facts_cholesterol"));} catch(SQLException e) {log.debug("No cholesterol for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setSodium(rs.getInt("nutrition_facts_sodium"));} catch(SQLException e) {log.debug("No sodium for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setPotassium(rs.getInt("nutrition_facts_potassium"));} catch(SQLException e) {log.debug("No potassium for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setTotal_carb(rs.getInt("nutrition_facts_total_carb"));} catch(SQLException e) {log.debug("No total_carb for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setDietary_fiber(rs.getInt("nutrition_facts_dietary_fiber"));} catch(SQLException e) {log.debug("No dietary_fiber for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setSugars(rs.getInt("nutrition_facts_sugars"));} catch(SQLException e) {log.debug("No sugars for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setSugar_alcohols(rs.getInt("nutrition_facts_sugar_alcohols"));} catch(SQLException e) {log.debug("No sugar_alcohols for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setProtein(rs.getInt("nutrition_facts_protein"));} catch(SQLException e) {log.debug("No protein for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setVitaminA(rs.getInt("nutrition_facts_vitaminA"));} catch(SQLException e) {log.debug("No vitaminA for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setVitaminB6(rs.getInt("nutrition_facts_vitaminB6"));} catch(SQLException e) {log.debug("No vitaminB6 for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setVitaminB12(rs.getInt("nutrition_facts_vitaminB12"));} catch(SQLException e) {log.debug("No vitaminB12 for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setVitaminC(rs.getInt("nutrition_facts_vitaminC"));} catch(SQLException e) {log.debug("No vitaminC for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setVitaminE(rs.getInt("nutrition_facts_vitaminE"));} catch(SQLException e) {log.debug("No vitaminE for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setVitaminK(rs.getInt("nutrition_facts_vitaminK"));} catch(SQLException e) {log.debug("No vitaminK for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setFolate(rs.getInt("nutrition_facts_folate"));} catch(SQLException e) {log.debug("No folate for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setMagnesium(rs.getInt("nutrition_facts_magnesium"));} catch(SQLException e) {log.debug("No magnesium for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setThiamin(rs.getInt("nutrition_facts_thiamin"));} catch(SQLException e) {log.debug("No thiamin for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setZinc(rs.getInt("nutrition_facts_zinc"));} catch(SQLException e) {log.debug("No zinc for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setCalcium(rs.getInt("nutrition_facts_calcium"));} catch(SQLException e) {log.debug("No calcium for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setRiboflavin(rs.getInt("nutrition_facts_riboflavin"));} catch(SQLException e) {log.debug("No riboflavin for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setBiotin(rs.getInt("nutrition_facts_biotin"));} catch(SQLException e) {log.debug("No biotin for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setIron(rs.getInt("nutrition_facts_iron"));} catch(SQLException e) {log.debug("No iron for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setNiacin(rs.getInt("nutrition_facts_niacin"));} catch(SQLException e) {log.debug("No niacin for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setPantothenic_acid(rs.getInt("nutrition_facts_pantothenic_acid"));} catch(SQLException e) {log.debug("No pantothenic_acid for nutrition facts '" + nutritionFacts.getId() + "'");}
		try {nutritionFacts.setPhosphorus(rs.getInt("nutrition_facts_phosphorus"));} catch(SQLException e) {log.debug("No phosphorus for nutrition facts '" + nutritionFacts.getId() + "'");}
		
		return nutritionFacts;
	}
}
