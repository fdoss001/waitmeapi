package com.waitme.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.waitme.config.WMProperties;
import com.waitme.domain.item.Item;
import com.waitme.domain.item.ItemInMeal;
import com.waitme.domain.item.ItemOption;
import com.waitme.domain.item.MainCategory;
import com.waitme.domain.item.Meal;
import com.waitme.domain.item.Menu;
import com.waitme.domain.item.OptionOnItem;
import com.waitme.domain.item.SubCategory;
import com.waitme.domain.restaurant.Table;
import com.waitme.utils.DBUtils;
import com.waitme.exception.DeleteException;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoResultException;

/**
 * Implementation persistence class for interacting with tables, menus, food, and other restaurant items
 * relating to restaurant management
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-27
 * @since 1.0 2019-02-27
 */
@Component
public class RestaurantManagementDAO {
	private Properties spProps;
	
	public RestaurantManagementDAO() {
		spProps = new WMProperties("storedproc.properties");		
	}

	
	/* *** TABLES *** */
	public List<Table> table_restaurant_sel_location(int companyid, int locationid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("table_restaurant_sel_location"), new Table(), locationid);
	}
	
	public int table_restaurant_ins(int companyid, String name, int locationid, int posx, int posy, String size, String shape, int capacity, int creatorid) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("table_restaurant_ins"), name, locationid, posx, posy, size, shape, capacity, creatorid);
	}
	
	public void table_restaurant_upd(int companyid, int tableid, String name, int locationid, int posx, int posy, String size, String shape, int capacity, boolean active, int updaterid) throws DuplicateException {
		DBUtils.updateHelper(companyid, spProps.getProperty("table_restaurant_upd"), tableid, name, locationid, posx, posy, size, shape, capacity, active, updaterid);
	}
	
	public void table_restaurant_del(int companyid, int tableid) throws DeleteException {
		DBUtils.deleteHelper(companyid, spProps.getProperty("table_restaurant_del"), tableid);
	}
	
	
	/* *** MENUS *** */
	public Menu menu_sel(int companyid, int menuid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("menu_sel"), new Menu(), menuid);
	}
	
	public List<Menu> menu_sel_min_location(int companyid, int locationid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("menu_sel_min_location"), new Menu(), locationid);
	}
	
	public int menu_ins(int companyid, String code, String name, boolean active, String dtmsAvailable, int locationid, int creatorid) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("menu_ins"), code, name, active, dtmsAvailable, locationid, creatorid);
	}
	
	public void menu_category_ins(int companyid, int menuid, int categoryid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("menu_category_ins"), menuid, categoryid);
	}
	
	public void menu_upd(int companyid, int menuid, String code, String name, boolean active, String dtmsAvailable, int locationid, int updaterid) throws DuplicateException {
		DBUtils.updateHelper(companyid, spProps.getProperty("menu_upd"), menuid, code, name, active, dtmsAvailable, locationid, updaterid);
	}
	
	public void menu_tog_act(int companyid, int menuid, boolean active, int updaterid) throws NoResultException {
		DBUtils.runSPHelper(companyid, spProps.getProperty("menu_tog_act"), menuid, active, updaterid);
	}
	
	public void menu_category_del_all_menu(int companyid, int menuid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("menu_category_del_all_menu"), menuid);
	}
	
	public List<MainCategory> menu_category_sel(int companyid, int menuid) throws NoResultException {
		//the categories for this menu will not include sub categories. Only one level deep
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("menu_category_sel"), new MainCategory(), menuid);
	}
	
	public void menu_del(int companyid, int menuid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("menu_del"), menuid);
	}
	/* *** END MENUS *** */
	
	
	/* *** MAIN CATEGORIES *** */
	public MainCategory category_sel(int companyid, int categoryid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("category_sel"), new MainCategory(), categoryid);
	}
	
	public List<MainCategory> category_sel_all_min(int companyid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("category_sel_all_min"), new MainCategory());
	}
	
	public int category_ins(int companyid, String code, String name, boolean active, int creatorid) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("category_ins"), code, name, active, creatorid);
	}
	
	public void category_sub_category_ins(int companyid, int categoryid, int subcategoryid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("category_sub_category_ins"), categoryid, subcategoryid);
	}
	
	public void category_upd(int companyid, int categoryid, String code, String name, boolean active, int updaterid) throws DuplicateException {
		DBUtils.updateHelper(companyid, spProps.getProperty("category_upd"), categoryid, code, name, active, updaterid);
	}
	
	public void category_tog_act(int companyid, int categoryid, boolean active, int updaterid) throws NoResultException {
		DBUtils.runSPHelper(companyid, spProps.getProperty("category_tog_act"), categoryid, active, updaterid);
	}
	
	public void category_sub_category_del_all_category(int companyid, int categoryid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("category_sub_category_del_all_category"), categoryid);
	}
	
	public List<SubCategory> category_sub_category_sel(int companyid, int categoryid) throws NoResultException {
		//the sub categories for this main category will not include meals. Only one level deep
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("category_sub_category_sel"), new SubCategory(), categoryid);
	}
	
	public void category_del(int companyid, int categoryid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("category_del"), categoryid);
	}
	/* *** END MAIN CATEGORIES *** */
	

	/* *** SUB CATEGORIES *** */
	public SubCategory sub_category_sel(int companyid, int subcategoryid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("sub_category_sel"), new SubCategory(), subcategoryid);
	}
	
	public List<SubCategory> sub_category_sel_all_min(int companyid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("sub_category_sel_all_min"), new SubCategory());
	}
	
	public int sub_category_ins(int companyid, String code, String name, boolean active, int creatorid) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("sub_category_ins"), code, name, active, creatorid);
	}
	
	public void sub_category_meal_ins(int companyid, int subcategoryid, int mealid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("sub_category_meal_ins"), subcategoryid, mealid);
	}
	
	public void sub_category_upd(int companyid, int subcategoryid, String code, String name, boolean active, int updaterid) throws DuplicateException {
		DBUtils.updateHelper(companyid, spProps.getProperty("sub_category_upd"), subcategoryid, code, name, active, updaterid);
	}

	public void sub_category_tog_act(int companyid, int subcategoryid, boolean active, int updaterid) throws NoResultException {
		DBUtils.runSPHelper(companyid, spProps.getProperty("sub_category_tog_act"), subcategoryid, active, updaterid);
	}
	
	public void sub_category_meal_del_all_sub_category(int companyid, int subcategoryid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("sub_category_meal_del_all_sub_category"), subcategoryid);
	}
	
	public List<Meal> sub_category_meal_sel(int companyid, int subCategoryid) throws NoResultException {
		//the meals for this sub category will not include items. Only one level deep
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("sub_category_meal_sel"), new Meal(), subCategoryid);
	}
	
	public void sub_category_del(int companyid, int subCategoryid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("sub_category_del"), subCategoryid);
	}
	/* *** END SUB CATEGORIES *** */
	

	/* *** MEALS *** */
	public Meal meal_sel(int companyid, int mealid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("meal_sel"), new Meal(), mealid);
	}
	
	public List<Meal> meal_sel_all_min(int companyid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("meal_sel_all_min"), new Meal());
	}
	
	public int meal_ins(int companyid, String code, String name, boolean active, int creatorid) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("meal_ins"), code, name, active, creatorid);
	}
	
	public void meal_item_ins(int companyid, int mealid, int itemid, BigDecimal priceAdjustment) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("meal_item_ins"), mealid, itemid, priceAdjustment);
	}
	
	public void meal_upd(int companyid, int mealid, String code, String name, boolean active, int updaterid) throws DuplicateException {
		DBUtils.updateHelper(companyid, spProps.getProperty("meal_upd"), mealid, code, name, active, updaterid);
	}

	public void meal_tog_act(int companyid, int mealid, boolean active, int updaterid) throws NoResultException {
		DBUtils.runSPHelper(companyid, spProps.getProperty("meal_tog_act"), mealid, active, updaterid);
	}
	
	public void meal_item_del_all_meal(int companyid, int mealid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("meal_item_del_all_meal"), mealid);
	}
	
	public List<ItemInMeal> meal_item_sel(int companyid, int mealid) throws NoResultException {
		//the items for this meal will not include options or substitutes. Only one level deep
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("meal_item_sel"), new ItemInMeal(), mealid);
	}
	
	public List<ItemInMeal> meal_item_substitute_sel(int companyid, int mealid, int itemid) throws NoResultException {
		//the items for this meal will not include options or substitutes. Only one level deep
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("meal_item_substitute_sel"), new ItemInMeal(), mealid, itemid);
	}
	
	public void meal_item_substitute_ins(int companyid, int mealid, int itemid, int substituteid, BigDecimal priceAdjustment) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("meal_item_substitute_ins"), mealid, itemid, substituteid, priceAdjustment);
	}
	
	public void meal_item_substitute_del_all_meal_item(int companyid, int mealid, int itemid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("meal_item_substitute_del_all_meal_item"), mealid, itemid);
	}
	
	public void meal_item_substitute_del_all_meal(int companyid, int mealid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("meal_item_substitute_del_all_meal"), mealid);
	}
	
	public void meal_del(int companyid, int mealid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("meal_del"), mealid);
	}
	/* *** END MEALS *** */
	
	
	/* *** ITEMS *** */
	public Item item_sel(int companyid, int itemid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("item_sel"), new Item(), itemid);
	}
	
	public List<Item> item_sel_all_min(int companyid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("item_sel_all_min"), new Item());
	}
	
	public List<ItemInMeal> meal_item_sel_all_arr(int companyid, String idarray) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("meal_item_sel_all_arr"), new ItemInMeal(), idarray);
	}
	
	public int item_ins(int companyid, String code, String name, String description, int nutritionFactsId, BigDecimal basePrice, boolean active, int creatorid) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("item_ins"), code, name, description, nutritionFactsId, basePrice, active, creatorid);
	}
	
	public void item_item_option_ins(int companyid, int itemid, int item_optionid, BigDecimal priceAdjustment, String quantity) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("item_item_option_ins"), itemid, item_optionid, priceAdjustment, quantity);
	}
	
	public void item_upd(int companyid, int itemid, String code, String name, String description, BigDecimal basePrice, boolean active, int updaterid) throws DuplicateException {
		DBUtils.updateHelper(companyid, spProps.getProperty("item_upd"), itemid, code, name, description, basePrice, active, updaterid);
	}

	public void item_tog_act(int companyid, int itemid, boolean active, int updaterid) throws NoResultException {
		DBUtils.runSPHelper(companyid, spProps.getProperty("item_tog_act"), itemid, active, updaterid);
	}
	
	public void item_item_option_del_all_item(int companyid, int itemid) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("item_item_option_del_all_item"), itemid);
	}
	
	public List<OptionOnItem> item_item_option_sel(int companyid, int itemid) throws NoResultException {
		//the items for this meal will not include options or substitutes. Only one level deep
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("item_item_option_sel"), new OptionOnItem(), itemid);
	}
	
	public void item_del(int companyid, int itemid) throws DeleteException {
		DBUtils.deleteHelper(companyid, spProps.getProperty("item_del"), itemid);
	}
	/* *** END ITEMS *** */

	
	/* *** OPTIONS *** */
	public ItemOption item_option_sel(int companyid, int optionid) throws NoResultException {
		return DBUtils.objectSelectHelper(companyid, spProps.getProperty("item_option_sel"), new ItemOption(), optionid);
	}
	
	public int item_option_sel_next_id(int companyid) throws NoResultException {
		return DBUtils.primitiveSelectHelper(companyid, spProps.getProperty("item_option_sel_next_id"), Integer.class);
	}
	
	public List<ItemOption> item_option_sel_all_min(int companyid) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("item_option_sel_all_min"), new ItemOption());
	}
	
	public List<OptionOnItem> item_item_option_sel_all_arr(int companyid, String idarray) throws NoResultException {
		return DBUtils.listSelectHelper(companyid, spProps.getProperty("item_item_option_sel_all_arr"), new OptionOnItem(), idarray);
	}
	
	public int item_option_ins(int companyid, String code, String name, String description, int nutritionFactsId, BigDecimal basePrice, boolean active, int creatorid) throws DuplicateException {
		return DBUtils.insertHelper(companyid, spProps.getProperty("item_option_ins"), code, name, description, nutritionFactsId, basePrice, active, creatorid);
	}
	
	public void item_option_upd(int companyid, int itemOptionId, String code, String name, String description, BigDecimal basePrice, boolean active, int updaterid) throws DuplicateException {
		DBUtils.updateHelper(companyid, spProps.getProperty("item_option_upd"), itemOptionId, code, name, description, basePrice, active, updaterid);
	}

	public void item_option_tog_act(int companyid, int itemOptionId, boolean active, int updaterid) throws NoResultException {
		DBUtils.runSPHelper(companyid, spProps.getProperty("item_option_tog_act"), itemOptionId, active, updaterid);
	}
	
	public void item_option_del(int companyid, int itemOptionId) throws DeleteException {
		DBUtils.deleteHelper(companyid, spProps.getProperty("item_option_del"), itemOptionId);
	}
	/* *** END OPTIONS *** */
	
	
	/* *** NUTRITION FACTS *** */
	public int nutrition_facts_ins(int companyid, String serving_size, Integer servings, int calories, Integer fat_calories, Integer total_fat, Integer saturated_fat, Integer trans_fat, Integer cholesterol, Integer sodium, Integer potassium, Integer total_carb, Integer dietary_fiber, Integer sugars, Integer sugar_alcohols, Integer protein, Integer vitaminA, Integer vitaminB6, Integer vitaminB12, Integer vitaminC, Integer vitaminE, Integer vitaminK, Integer folate, Integer magnesium, Integer thiamin, Integer zinc, Integer calcium, Integer riboflavin, Integer biotin, Integer iron, Integer niacin, Integer pantothenic_acid, Integer phosphorus) throws DuplicateException {		
		return DBUtils.insertHelper(companyid, spProps.getProperty("nutrition_facts_ins"), serving_size, servings, calories, fat_calories, total_fat, saturated_fat, trans_fat, cholesterol, sodium, potassium, total_carb, dietary_fiber, sugars, sugar_alcohols, protein, vitaminA, vitaminB6, vitaminB12, vitaminC, vitaminE, vitaminK, folate, magnesium, thiamin, zinc, calcium, riboflavin, biotin, iron, niacin, pantothenic_acid, phosphorus);
	}
	
	public void nutrition_facts_upd(int companyid, int nutritionFactsId, String serving_size, Integer servings, int calories, Integer fat_calories, Integer total_fat, Integer saturated_fat, Integer trans_fat, Integer cholesterol, Integer sodium, Integer potassium, Integer total_carb, Integer dietary_fiber, Integer sugars, Integer sugar_alcohols, Integer protein, Integer vitaminA, Integer vitaminB6, Integer vitaminB12, Integer vitaminC, Integer vitaminE, Integer vitaminK, Integer folate, Integer magnesium, Integer thiamin, Integer zinc, Integer calcium, Integer riboflavin, Integer biotin, Integer iron, Integer niacin, Integer pantothenic_acid, Integer phosphorus) {		
		DBUtils.runSPHelper(companyid, spProps.getProperty("nutrition_facts_upd"), nutritionFactsId, serving_size, servings, calories, fat_calories, total_fat, saturated_fat, trans_fat, cholesterol, sodium, potassium, total_carb, dietary_fiber, sugars, sugar_alcohols, protein, vitaminA, vitaminB6, vitaminB12, vitaminC, vitaminE, vitaminK, folate, magnesium, thiamin, zinc, calcium, riboflavin, biotin, iron, niacin, pantothenic_acid, phosphorus);
	}
	
	public void nutrition_facts_del(int companyid, int nutritionFactsId) {
		DBUtils.runSPHelper(companyid, spProps.getProperty("nutrition_facts_del"), nutritionFactsId);
	}
	/* *** END NUTRITION FACTS *** */
}
