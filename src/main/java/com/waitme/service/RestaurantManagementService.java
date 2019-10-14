package com.waitme.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.waitme.domain.item.FoodListHolder;
import com.waitme.domain.item.Item;
import com.waitme.domain.item.ItemOption;
import com.waitme.domain.item.MainCategory;
import com.waitme.domain.item.Meal;
import com.waitme.domain.item.Menu;
import com.waitme.domain.item.NutritionFacts;
import com.waitme.domain.item.OptionOnItem;
import com.waitme.config.WMProperties;
import com.waitme.domain.item.SubCategory;
import com.waitme.domain.restaurant.Table;
import com.waitme.domain.user.WMUser;
import com.waitme.domain.item.ItemInMeal;
import com.waitme.persistence.RestaurantManagementDAO;
import com.waitme.exception.DeleteException;
import com.waitme.exception.DuplicateException;
import com.waitme.exception.NoResultException;
/**
 * Service class for restaurant management related functions
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-27
 * @since 1.0 2019-02-27
 */
@Service
public class RestaurantManagementService {
	@Autowired
	RestaurantManagementDAO restaurantManagementDAO;
	private Logger log = LoggerFactory.getLogger(RestaurantManagementService.class);
	private Properties messageProps = new WMProperties("messages.properties");
	
	public RestaurantManagementService() {}
	
	public RestaurantManagementService(RestaurantManagementDAO rmd) {
		restaurantManagementDAO = rmd;
	}
	
	/* *** TABLES *** */
	/**
	 * Gets the tables that exist in the given location from DB
	 * These tables do not have any hosting related details such as party
	 * @param locationid location to check for
	 * @return the list of tables 
	 * @throws NoResultException if there are no tables
	 */
	public List<Table> getTables(int companyid, int locationid) throws NoResultException {
		List<Table> tables;
		try {
			log.debug("Getting all tables.");
			tables = restaurantManagementDAO.table_restaurant_sel_location(companyid, locationid);
		} catch (NoResultException e) {
			log.debug("There are no tables for this location '" + locationid + "'");
			throw new NoResultException(messageProps.getProperty("error.no_tables"));
		}
		log.debug("Successfully got all (" + tables.size() + ") tables.");
		return tables;
	}
	
	/**
	 * Creates a new restaurant table in the the DB
	 * @param table the new table to insert
	 * @param creator the creator of this table
	 * @return the new table with the correct id
	 * @throws DuplicateException if a table with that name in that location already exists
	 */
	public Table createNewTable(Table table, WMUser creator) throws DuplicateException {
		log.debug("Creating new table '" + table.getName() + "' in the DB.");
		int id = 0;
		String size = "" + table.getSize().get(0);
		if (table.getSize().size() > 1)
			size += "," + table.getSize().get(1);
		
		try {
			id = restaurantManagementDAO.table_restaurant_ins(creator.getCompany().getId(), table.getName(), table.getLocationid(), table.getPosition().get(0), table.getPosition().get(1), size, table.getShape().toString(), table.getCapacity(), creator.getId());
		} catch (DuplicateException e) {
			throw new DuplicateException(messageProps.getProperty("error.duplicate_name"));
		}
		table.setId(id);
		log.debug("Successfully created table");
		return table;
	}
	
	/**
	 * Permanently deletes a table from the DB.
	 * WARNING! This should rarely be done. All party/order history must be deleted first
	 * Use with extreme caution. Deactivating a table is usually preferred. 
	 * @param tableid the id of the table to be deleted
	 * @throws DeleteException if the table cannot be deleted because it has party/order history.
	 */
	public void deleteTable(int companyid, int tableid) throws DeleteException {
		try {
			restaurantManagementDAO.table_restaurant_del(companyid, tableid);
		} catch (DeleteException e) {
			log.error("Could not delete due to constraints. All required related objects must be deleted or reassigned first.");
			throw new DeleteException("error;" + e.getMessage() + " Please delete before proceeding.");
		}
	}
	
	/**
	 * Updates the table in the DB
	 * @param table the table to update
	 * @param updater the user who is updating
	 * @throws DuplicateException if a table with that name in that location already exists
	 */
	public void updateTable(Table table, WMUser updater) throws DuplicateException {
		log.debug("Updating table '" + table.getId() + "'");
		String size = "" + table.getSize().get(0);
		if (table.getSize().size() > 1)
			size += "," + table.getSize().get(1);
		restaurantManagementDAO.table_restaurant_upd(updater.getCompany().getId(), table.getId(), table.getName(), table.getLocationid(), table.getPosition().get(0), table.getPosition().get(1), size, table.getShape().toString(), table.getCapacity(), table.isActive(), updater.getId());
		log.debug("Successfully updated table.");
	}
	
	
	/* *** MENUS *** */
	/**
	 * Gets all the menus and its main categories
	 * @param locationid the location of the menus to select
	 * @return the list of menus
	 */
	public List<Menu> getAllMenusMinimal(int companyid, int locationid) throws NoResultException {		
		log.debug("Getting all menus.");
		List<Menu> menus = restaurantManagementDAO.menu_sel_min_location(companyid, locationid);
		log.debug("Successfully got all (" + menus.size() + ") menus.");
		return menus;		
	}
	
	/**
	 * Gets a menu for the given menu id
	 * @param menuid the id of the menu to get
	 * @return the menu object with the main categories
	 * @throws NoResultException if the menu with id is not found
	 */
	public Menu getMenu(int companyid, int menuid) throws NoResultException {
		Menu menu = restaurantManagementDAO.menu_sel(companyid, menuid);
		
		log.debug("\tFound menu '" + menu.getName() + "'. Getting main categories for it.");
		List<MainCategory> menuCategories = new ArrayList<MainCategory>(0);
		try {
			menuCategories = restaurantManagementDAO.menu_category_sel(companyid, menu.getId());
		} catch (NoResultException e) {log.debug("Menu '" + menu.getName() + "' has main no categories. Continuing...");}
		log.debug("\tSuccessfully got all (" + menuCategories.size() + ") main categories for menu '" + menu.getName() + "'");
		menu.setCategories(menuCategories);
		
		return menu;
	}
	
	/**
	 * Creates a new menu and inserts in in the DB
	 * @param menu the menu to insert
	 * @param creator the creator of this menu
	 * @return the created menu object
	 */
	public Menu createNewMenu(Menu menu, WMUser creator) throws DuplicateException {
		log.debug("Creating new menu '" + menu.getName() + "' in the DB.");
		//the DB insert
		try {
			int id = restaurantManagementDAO.menu_ins(creator.getCompany().getId(), menu.getCode(), menu.getName(), menu.isActive(), menu.getDateTimesAvailable().toString(), menu.getLocationid(), creator.getId());
			for (MainCategory mc : menu.getCategories()) {
				log.debug("\tInserting main category '" + mc.getName() + "' for menu.");
				restaurantManagementDAO.menu_category_ins(creator.getCompany().getId(), id, mc.getId());
			}
			log.debug("Successfully inserted '" + menu.getCategories().size() + "' main categories for menu");
			log.debug("Successfully created menu");
			menu.setId(id);
			return menu;
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));			
		}
	}
	
	/**
	 * Updates the menu in the database with the given menu values 
	 * @param menu the new menu
	 * @param updater the updater of this menu
	 */
	public void updateMenu(Menu menu, WMUser updater) throws DuplicateException {
		log.debug("Updating menu '" + menu.getId() + "'");
		try {
			//the DB update
			restaurantManagementDAO.menu_upd(updater.getCompany().getId(), menu.getId(), menu.getCode(), menu.getName(), menu.isActive(), menu.getDateTimesAvailable().toString(), menu.getLocationid(), updater.getId());
			restaurantManagementDAO.menu_category_del_all_menu(updater.getCompany().getId(), menu.getId());
			for (MainCategory mc : menu.getCategories()) {
				log.debug("\tInserting main category '" + mc.getName() + "' for menu.");
				restaurantManagementDAO.menu_category_ins(updater.getCompany().getId(), menu.getId(), mc.getId());
			}
			log.debug("Successfully inserted '" + menu.getCategories().size() + "' main categories for menu");
			log.debug("Successfully updated location.");
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));			
		}
	}
	
	/**
	 * Permanently deletes a menu from the DB.
	 * Deactivating a menu is usually preferred. 
	 * @param menuid the id of the menu to be deleted
	 */
	public void deleteMenu(int companyid, int menuid) {
		log.debug("Permanently deleting menu '" + menuid + "' and all their related objects.");
		restaurantManagementDAO.menu_del(companyid, menuid);
		log.debug("Successfully deleted menu.");
	}
	
	
	/* *** CATEGORIES *** */
	/**
	 * Gets all the main categories and their sub categories
	 * @param subCategories all sub categories
	 * @return the list of main categories
	 */
	public List<MainCategory> getAllMainCategoriesMinimal(int companyid) throws NoResultException {
		log.debug("Getting all main categories.");
		List<MainCategory> mainCategories = restaurantManagementDAO.category_sel_all_min(companyid);
		log.debug("Successfully got all (" + mainCategories.size() + ") main categories.");
		return mainCategories;
	}
	
	
	/**
	 * Gets a main category for the given category id
	 * @param categoryid the id of the main category to get
	 * @return the category object with the sub categories
	 * @throws NoResultException if the category with id is not found
	 */
	public MainCategory getMainCategory(int companyid, int categoryid) throws NoResultException {
		MainCategory mainCategory = restaurantManagementDAO.category_sel(companyid, categoryid);
		
		log.debug("\tFound main category '" + mainCategory.getName() + "'. Getting sub categories for it.");
		List<SubCategory> mainSubCats = new ArrayList<SubCategory>(0); 
		try {
			mainSubCats = restaurantManagementDAO.category_sub_category_sel(companyid, mainCategory.getId());
		} catch (NoResultException e) {log.debug("Main category '" + mainCategory.getName() + "' has no sub categories. Continuing...");}
		
		log.debug("\tSuccessfully got all (" + mainSubCats.size() + ") sub categories for main category '" + mainCategory.getName() + "'");
		mainCategory.setSubCategories(mainSubCats);
		
		return mainCategory;
	}
	
	/**
	 * Gets all the sub categories and their meals
	 * @return the list of sub categories
	 */
	public List<SubCategory> getAllSubCategoriesMinimal(int companyid) throws NoResultException {
		log.debug("Getting all sub categories.");
		List<SubCategory> subCategories = restaurantManagementDAO.sub_category_sel_all_min(companyid);
		log.debug("Successfully got all (" + subCategories.size() + ") sub categories.");
		return subCategories;
	}
	
	/**
	 * Gets a sub category for the given category id
	 * @param categoryid the id of the sub category to get
	 * @return the sub category object with the meals
	 * @throws NoResultException if the category with id is not found
	 */
	public SubCategory getSubCategory(int companyid, int categoryid) throws NoResultException {
		SubCategory subCategory = restaurantManagementDAO.sub_category_sel(companyid, categoryid);
		
		log.debug("\tFound sub category '" + subCategory.getName() + "'. Getting meals for it.");
		List<Meal> subCatMeals = new ArrayList<Meal>(0);
		try {
			subCatMeals = restaurantManagementDAO.sub_category_meal_sel(companyid, subCategory.getId());
		} catch (NoResultException e) {log.debug("Sub category '" + subCategory.getName() + "' has no meals. Continuing...");}
		
		log.debug("\tSuccessfully got all (" + subCatMeals.size() + ") meals for sub category '" + subCategory.getName() + "'");
		subCategory.setMeals(subCatMeals);
		
		return subCategory;
	}
	
	/**
	 * Creates a new category or sub category and inserts it in the DB
	 * @param category the category to insert
	 * @param creator the creator of this category
	 * @return the category object made
	 */
	public FoodListHolder createNewCategory(FoodListHolder category, WMUser creator) throws DuplicateException {
		int id = 0;
		try {
			//the DB insert
			if (category instanceof MainCategory) {
				log.debug("Creating new main category '" + category.getName() + "' in the DB.");
				id = restaurantManagementDAO.category_ins(creator.getCompany().getId(), category.getCode(), category.getName(), category.isActive(), creator.getId());
				for (SubCategory sc : ((MainCategory) category).getSubCategories()) {
					log.debug("\tInserting sub category '" + sc.getName() + "' for main category.");
					restaurantManagementDAO.category_sub_category_ins(creator.getCompany().getId(), id, sc.getId());
				}
				log.debug("Successfully inserted '" + ((MainCategory) category).getSubCategories().size() + "' sub categories for main category");				
				log.debug("Successfully created main category");
			}
			else if (category instanceof SubCategory) {
				log.debug("Creating sub category '" + category.getName() + "' in the DB.");
				id = restaurantManagementDAO.sub_category_ins(creator.getCompany().getId(), category.getCode(), category.getName(), category.isActive(), creator.getId());
				for (Meal meal : ((SubCategory) category).getMeals()) {
					log.debug("\tInserting meal '" + meal.getName() + "' for sub category.");
					restaurantManagementDAO.sub_category_meal_ins(creator.getCompany().getId(), id, meal.getId());
				}
				log.debug("Successfully inserted '" + ((SubCategory) category).getMeals().size() + "' meals for sub category");
				SubCategory sc = new SubCategory(id, category.getCode(), category.getName(), category.isActive());
				sc.setMeals(((SubCategory) category).getMeals());
				log.debug("Successfully created sub category");
			}
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));			
		}
		category.setId(id);
		return category;
	}
	
	/**
	 * Updates a given category in the database
	 * @param category the new category
	 * @param updater the updater of this category
	 */
	public void updateCategory(FoodListHolder category, WMUser updater) throws DuplicateException {
		try {
			//the DB update
			if (category instanceof MainCategory) {
				log.debug("Updating main category '" + category.getId() + "'");
				restaurantManagementDAO.category_upd(updater.getCompany().getId(), category.getId(), category.getCode(), category.getName(), category.isActive(), updater.getId());
				restaurantManagementDAO.category_sub_category_del_all_category(updater.getCompany().getId(), category.getId());
				for (SubCategory sc : ((MainCategory) category).getSubCategories()) {
					log.debug("\tInserting sub category '" + sc.getName() + "' for main category.");
					restaurantManagementDAO.category_sub_category_ins(updater.getCompany().getId(), category.getId(), sc.getId());
				}
				log.debug("Successfully inserted '" + ((MainCategory) category).getSubCategories().size() + "' sub categories for main category");
				log.debug("Successfully updated main category.");
			}
			else if (category instanceof SubCategory) {
				log.debug("Updating sub category '" + category.getId() + "'");
				restaurantManagementDAO.sub_category_upd(updater.getCompany().getId(), category.getId(), category.getCode(), category.getName(), category.isActive(), updater.getId());
				restaurantManagementDAO.sub_category_meal_del_all_sub_category(updater.getCompany().getId(), category.getId());
				for (Meal meal : ((SubCategory) category).getMeals()) {
					log.debug("\tInserting meal '" + meal.getName() + "' for sub category.");
					restaurantManagementDAO.sub_category_meal_ins(updater.getCompany().getId(), category.getId(), meal.getId());
				}
				log.debug("Successfully inserted '" + ((SubCategory) category).getMeals().size() + "' meals for sub category");
				log.debug("Successfully updated sub category.");
			}
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));			
		}
	}
	
	/**
	 * Permanently deletes a main category from the DB.
	 * Deactivating a main category is usually preferred. 
	 * @param mainCategoryId the id of the main category to be deleted
	 */
	public void deleteMainCategory(int companyid, int mainCategoryId) {
		log.debug("Permanently deleting main category '" + mainCategoryId + "' and all their related objects.");
		restaurantManagementDAO.category_del(companyid, mainCategoryId);
		log.debug("Successfully deleted main category.");
	}
	
	/**
	 * Permanently deletes a subcategory from the DB.
	 * Deactivating a subcategory is usually preferred. 
	 * @param subCategoryId the id of the subcategory to be deleted
	 */
	public void deleteSubCategory(int companyid, int subCategoryId) {
		log.debug("Permanently deleting sub category '" + subCategoryId + "' and all their related objects.");
		restaurantManagementDAO.sub_category_del(companyid, subCategoryId);
		log.debug("Successfully deleted sub category.");
	}
	
	
	/* *** MEALS *** */
	/**
	 * Gets all the meals and their items
	 * @return list of all meals
	 */
	public List<Meal> getAllMealsMinimal(int companyid) throws NoResultException {
		log.debug("Getting all meals.");
		List<Meal> meals = restaurantManagementDAO.meal_sel_all_min(companyid);
		log.debug("Successfully got all (" + meals.size() + ") meals.");
		return meals;		
	}
	
	/**
	 * Gets a meal for the given meal id
	 * @param mealid the id of the meal to get
	 * @return the meal object with the items
	 * @throws NoResultException if the meal with id is not found
	 */
	public Meal getMeal(int companyid, int mealid) throws NoResultException {
		Meal meal = restaurantManagementDAO.meal_sel(companyid, mealid);
		
		log.debug("\tFound meal '" + meal.getName() + "'. Getting items for it.");
		List<ItemInMeal> mealItems = new ArrayList<ItemInMeal>(0);
		try {
			mealItems = restaurantManagementDAO.meal_item_sel(companyid, meal.getId());
		} catch (NoResultException e) {log.debug("Meal '" + meal.getName() + "' has no items. Continuing...");}
		
		for (ItemInMeal mealItem : mealItems) {
			log.debug("\t\tFound item '" + mealItem.getName() + "' for meal '" + meal.getName() + "'. Getting substitutes for it.");
			List<ItemInMeal> substituteChoices = new ArrayList<ItemInMeal>(0);
			try {
				substituteChoices = restaurantManagementDAO.meal_item_substitute_sel(companyid, meal.getId(), mealItem.getId());
			} catch (NoResultException e) {log.debug("Meal item '" + mealItem.getName() + "' has no substitutes. Continuing...");}
							
			log.debug("\t\tSuccessfully got all (" + substituteChoices.size() + ") substitutes for meal item '" + mealItem.getName() + "'");
			mealItem.setSubstituteChoices(substituteChoices);
		}
		log.debug("\tSuccessfully got all (" + mealItems.size() + ") items for meal '" + meal.getName() + "'");
		meal.setMealItems(mealItems);
		
		return meal;
	}
	
	/**
	 * Creates a new meal and inserts it in the DB
	 * @param meal the meal to insert
	 * @param creator the creator of this meal
	 * @return the meal object made
	 */
	public Meal createNewMeal(Meal meal, WMUser creator) throws DuplicateException {
		log.debug("Creating new meal '" + meal.getName() + "' in the DB.");
		try {
			//the DB insert
			int id = restaurantManagementDAO.meal_ins(creator.getCompany().getId(), meal.getCode(), meal.getName(), meal.isActive(), creator.getId());		
			for (ItemInMeal mi : meal.getMealItems()) {
				log.debug("\tInserting item '" + mi.getName() + "' for meal");
				restaurantManagementDAO.meal_item_ins(creator.getCompany().getId(), id, mi.getId(), mi.getPriceAdjustment());
				for (ItemInMeal i : mi.getSubstituteChoices()) {
					log.debug("\t\tInserting substitute '" + i.getName() + "' for item");
					restaurantManagementDAO.meal_item_substitute_ins(creator.getCompany().getId(), id, mi.getId(), i.getId(), i.getPriceAdjustment());
				}
				log.debug("\tSuccessfully inserted '" + mi.getSubstituteChoices().size() + "' substitutes for item");
			}
			log.debug("Successfully inserted '" + meal.getMealItems().size() + "' items for meal");
			meal.setId(id);
			log.debug("Successfully created meal");
			return meal;
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));			
		}
	}
	
	/**
	 * Updates the meal in the database with the given meal values 
	 * @param meal the new meal
	 * @param updater the updater of this meal
	 */
	public void updateMeal(Meal meal, WMUser updater) throws DuplicateException {
		log.debug("Updating meal '" + meal.getId() + "'");
		try {
			//the DB update
			restaurantManagementDAO.meal_upd(updater.getCompany().getId(), meal.getId(), meal.getCode(), meal.getName(), meal.isActive(), updater.getId());
			restaurantManagementDAO.meal_item_substitute_del_all_meal(updater.getCompany().getId(), meal.getId());
			restaurantManagementDAO.meal_item_del_all_meal(updater.getCompany().getId(), meal.getId());
			for (ItemInMeal mi : meal.getMealItems()) {
				log.debug("\tInserting item '" + mi.getName() + "' for meal");
				restaurantManagementDAO.meal_item_ins(updater.getCompany().getId(), meal.getId(), mi.getId(), mi.getPriceAdjustment());
				restaurantManagementDAO.meal_item_substitute_del_all_meal_item(updater.getCompany().getId(), meal.getId(), mi.getId());
				for (ItemInMeal i : mi.getSubstituteChoices()) {
					log.debug("\t\tInserting substitute '" + i.getName() + "' for item");
					restaurantManagementDAO.meal_item_substitute_ins(updater.getCompany().getId(), meal.getId(), mi.getId(), i.getId(), i.getPriceAdjustment());
				}
				log.debug("\tSuccessfully inserted '" + mi.getSubstituteChoices().size() + "' substitutes for item");
			}
			log.debug("Successfully inserted '" + meal.getMealItems().size() + "' items for meal");
			log.debug("Successfully updated meal.");
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));			
		}
	}

	/**
	 * Permanently deletes a meal from the DB.
	 * Deactivating a meal is usually preferred. 
	 * @param mealid the id of the meal to be deleted
	 */
	public void deleteMeal(int companyid, int mealid) {
		log.debug("Permanently deleting meal '" + mealid + "' and all their related objects.");
		restaurantManagementDAO.meal_del(companyid, mealid);
		log.debug("Successfully deleted meal.");
	}
	
	
	/* *** ITEMS *** */	
	/**
	 * Gets all the items and their options
	 * @return list of all items
	 */
	public List<Item> getAllItemsMinimal(int companyid) throws NoResultException {
		log.debug("Getting all items.");
		List<Item> items = restaurantManagementDAO.item_sel_all_min(companyid);
		log.debug("Successfully got all (" + items.size() + ") items.");
		return items;		
	}
	
	/**
	 * Gets all the meal items in the given id array
	 * @param idarray the list of all ids of the options to get
	 * @return list of options
	 */
	public List<ItemInMeal> getAllMealItemsById(int companyid, int[] idarray) throws NoResultException {
		String strIdArr = Arrays.toString(idarray).replace("[","").replace("]","").replace(" ","");
		log.debug("Getting options in " + strIdArr);
		List<ItemInMeal> mealItems = restaurantManagementDAO.meal_item_sel_all_arr(companyid, strIdArr);
		for (ItemInMeal mealItem : mealItems) { //give them empty arrays as no meal has been chosen yet
			mealItem.setSubstituteChoices(new ArrayList<ItemInMeal>(0));
			mealItem.setOptions(new ArrayList<OptionOnItem>(0));
		}
		log.debug("Successfully got '" + mealItems.size() + "' options.");
		return mealItems;		
	}
	
	/**
	 * Gets an item for the given item id
	 * @param itemid the id of the item to get
	 * @return the item object with the options
	 * @throws NoResultException if the item with id is not found
	 */
	public Item getItem(int companyid, int itemid) throws NoResultException {
		Item item = restaurantManagementDAO.item_sel(companyid, itemid);
		
		log.debug("\tFound item '" + item.getName() + "'. Getting options for it.");
		List<OptionOnItem> options = new ArrayList<OptionOnItem>(0);
		try {
			options = restaurantManagementDAO.item_item_option_sel(companyid, item.getId());
		} catch (NoResultException e) {log.debug("Item '" + item.getName() + "' has no options. Continuing...");}
		log.debug("Successfully got all (" + options.size() + ") options for item '" + item.getName() + "'");
		item.setOptions(options);
		
		return item;
	}
	
	/**
	 * Creates a new item and inserts it in the DB
	 * @param item the item to insert
	 * @param creator the creator of this item
	 * @return the item object made
	 */
	public Item createNewItem(Item item, WMUser creator) throws DuplicateException {		
		log.debug("Creating new item '" + item.getName() + "' in the DB.");
		
		//nutrition facts
		NutritionFacts nf = item.getNutritionFacts();
		int nutritionFactsId = restaurantManagementDAO.nutrition_facts_ins(creator.getCompany().getId(),nf.getServing_size(),nf.getServings(),nf.getCalories(),nf.getFat_calories(),nf.getTotal_fat(), nf.getSaturated_fat(),nf.getTrans_fat(), nf.getCholesterol(), nf.getSodium(), nf.getPotassium(),nf.getTotal_carb(), nf.getDietary_fiber(),nf.getSugars(), nf.getSugar_alcohols(),nf.getProtein(), nf.getVitaminA(),nf.getVitaminB6(), nf.getVitaminB12(),nf.getVitaminC(), nf.getVitaminE(),nf.getVitaminK(),nf.getFolate(), nf.getMagnesium(),nf.getThiamin(),nf.getZinc(),nf.getCalcium(),nf.getRiboflavin(),nf.getBiotin(),nf.getIron(),nf.getNiacin(),nf.getPantothenic_acid(),nf.getPhosphorus());
		nf.setId(nutritionFactsId);
		
		try {
			//the DB insert
			int id = restaurantManagementDAO.item_ins(creator.getCompany().getId(), item.getCode(), item.getName(), item.getDescription(), nutritionFactsId, item.getPrice(), item.isActive(), creator.getId());		
			for (OptionOnItem op : item.getOptions()) {
				log.debug("\tInserting option '" + op.getName() + "' for item.");
				restaurantManagementDAO.item_item_option_ins(creator.getCompany().getId(), id, op.getId(), op.getPriceAdjustment(), op.getQuantity().toString());
			}
			log.debug("Successfully inserted '" + item.getOptions().size() + "' options for item");
			item.setId(id);
			item.setNutritionFacts(nf);
			log.debug("Successfully created item");
			return item;
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			restaurantManagementDAO.nutrition_facts_del(creator.getCompany().getId(), nutritionFactsId);
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));			
		}		
	}
	
	/**
	 * Updates the item in the database with the given item values 
	 * @param item the new item
	 * @param updater the updater of this item
	 */
	public void updateItem(Item item, WMUser updater) throws DuplicateException {
		log.debug("Updating item '" + item.getId() + "'");
		try {
			//the DB update
			restaurantManagementDAO.item_upd(updater.getCompany().getId(), item.getId(), item.getCode(), item.getName(), item.getDescription(), item.getPrice(), item.isActive(), updater.getId());
			restaurantManagementDAO.item_item_option_del_all_item(updater.getCompany().getId(), item.getId());
			for (OptionOnItem op : item.getOptions()) {
				log.debug("\tInserting option '" + op.getName() + "' for item.");
				restaurantManagementDAO.item_item_option_ins(updater.getCompany().getId(), item.getId(), op.getId(), op.getPriceAdjustment(), op.getQuantity().toString());
			}
			log.debug("Successfully inserted '" + item.getOptions().size() + "' options for item");
			
			//nutrition facts
			NutritionFacts nf = item.getNutritionFacts();
			restaurantManagementDAO.nutrition_facts_upd(updater.getCompany().getId(),nf.getId(),nf.getServing_size(),nf.getServings(),nf.getCalories(),nf.getFat_calories(),nf.getTotal_fat(), nf.getSaturated_fat(),nf.getTrans_fat(), nf.getCholesterol(), nf.getSodium(), nf.getPotassium(),nf.getTotal_carb(), nf.getDietary_fiber(),nf.getSugars(), nf.getSugar_alcohols(),nf.getProtein(), nf.getVitaminA(),nf.getVitaminB6(), nf.getVitaminB12(),nf.getVitaminC(), nf.getVitaminE(),nf.getVitaminK(),nf.getFolate(), nf.getMagnesium(),nf.getThiamin(),nf.getZinc(),nf.getCalcium(),nf.getRiboflavin(),nf.getBiotin(),nf.getIron(),nf.getNiacin(),nf.getPantothenic_acid(),nf.getPhosphorus());
			log.debug("Successfully updated item.");
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));			
		}		
	}
	
	/**
	 * Permanently deletes an item from the DB.
	 * WARNING! This should rarely be done. All order history with this item must be deleted first
	 * Use with extreme caution. Deactivating an item is usually preferred. 
	 * @param itemid the id of the item to be deleted
	 * @throws DeleteException if the item cannot be deleted because it has order history
	 */
	public void deleteItem(int companyid, int itemid) throws DeleteException {
		log.debug("Permanently deleting item '" + itemid + "' and all their related objects.");
		try {
			restaurantManagementDAO.item_del(companyid, itemid);
		} catch (DeleteException e) {
			log.error("Could not delete due to constraints. All required related objects must be deleted first.");
			throw new DeleteException("error;" + e.getMessage() + " Please delete before proceeding.");
		}
		log.debug("Successfully deleted item.");
	}
	
	
	/* *** OPTIONS *** */
	/**
	 * Gets all the item options
	 * @return list of all options
	 */
	public List<ItemOption> getAllItemOptionsMinimal(int companyid) throws NoResultException {
		log.debug("Getting all options.");
		List<ItemOption> itemOptions = restaurantManagementDAO.item_option_sel_all_min(companyid);		
		log.debug("Successfully got all (" + itemOptions.size() + ") options.");
		return itemOptions;		
	}
	
	/**
	 * Gets all the item options in the given id array
	 * @param idarray the list of all ids of the options to get
	 * @return list of options
	 */
	public List<OptionOnItem> getAllItemOptionsById(int companyid, int[] idarray) throws NoResultException {
		String strIdArr = Arrays.toString(idarray).replace("[","").replace("]","").replace(" ","");
		log.debug("Getting options in " + strIdArr);
		List<OptionOnItem> itemOptions = restaurantManagementDAO.item_item_option_sel_all_arr(companyid, strIdArr);
		log.debug("Successfully got '" + itemOptions.size() + "' options.");
		return itemOptions;		
	}
	
	/**
	 * Gets an option for the given option id
	 * @param optionid the id of the option to get
	 * @return the option object
	 * @throws NoResultException if the option with id is not found
	 */
	public ItemOption getItemOption(int companyid, int optionid) throws NoResultException {
		ItemOption item = restaurantManagementDAO.item_option_sel(companyid, optionid);		
		return item;
	}
	
	/**
	 * Creates a new option and inserts it in the DB
	 * @param option the option to insert
	 * @param creator the creator of this option
	 * @return the option object made
	 */
	public ItemOption createNewOption(ItemOption option, WMUser creator) throws DuplicateException {		
		log.debug("Creating new option '" + option.getName() + "' in the DB.");

		//nutrition facts
		NutritionFacts nf = option.getNutritionFacts();
		int nutritionFactsId = restaurantManagementDAO.nutrition_facts_ins(creator.getCompany().getId(),nf.getServing_size(),nf.getServings(),nf.getCalories(),nf.getFat_calories(),nf.getTotal_fat(), nf.getSaturated_fat(),nf.getTrans_fat(), nf.getCholesterol(), nf.getSodium(), nf.getPotassium(),nf.getTotal_carb(), nf.getDietary_fiber(),nf.getSugars(), nf.getSugar_alcohols(),nf.getProtein(), nf.getVitaminA(),nf.getVitaminB6(), nf.getVitaminB12(),nf.getVitaminC(), nf.getVitaminE(),nf.getVitaminK(),nf.getFolate(), nf.getMagnesium(),nf.getThiamin(),nf.getZinc(),nf.getCalcium(),nf.getRiboflavin(),nf.getBiotin(),nf.getIron(),nf.getNiacin(),nf.getPantothenic_acid(),nf.getPhosphorus());
		nf.setId(nutritionFactsId);
		
		try {
			//the DB insert
			int id = restaurantManagementDAO.item_option_ins(creator.getCompany().getId(), option.getCode(), option.getName(), option.getDescription(), nutritionFactsId, option.getPrice(), option.isActive(), creator.getId());			
			option.setId(id);
			log.debug("Successfully created option");
			return option;
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			restaurantManagementDAO.nutrition_facts_del(creator.getCompany().getId(), nutritionFactsId);
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));			
		}
	}
	
	/**
	 * Updates the option in the database with the given option values 
	 * @param option the new option
	 * @param updater the updater of this option
	 */
	public void updateOption(ItemOption option, WMUser updater) throws DuplicateException {
		log.debug("Updating option '" + option.getId() + "'");
		try {
			//the DB update
			restaurantManagementDAO.item_option_upd(updater.getCompany().getId(), option.getId(), option.getCode(), option.getName(), option.getDescription(), option.getPrice(), option.isActive(), updater.getId());
			//nutrition facts
			NutritionFacts nf = option.getNutritionFacts();
			restaurantManagementDAO.nutrition_facts_upd(updater.getCompany().getId(),nf.getId(),nf.getServing_size(),nf.getServings(),nf.getCalories(),nf.getFat_calories(),nf.getTotal_fat(), nf.getSaturated_fat(),nf.getTrans_fat(), nf.getCholesterol(), nf.getSodium(), nf.getPotassium(),nf.getTotal_carb(), nf.getDietary_fiber(),nf.getSugars(), nf.getSugar_alcohols(),nf.getProtein(), nf.getVitaminA(),nf.getVitaminB6(), nf.getVitaminB12(),nf.getVitaminC(), nf.getVitaminE(),nf.getVitaminK(),nf.getFolate(), nf.getMagnesium(),nf.getThiamin(),nf.getZinc(),nf.getCalcium(),nf.getRiboflavin(),nf.getBiotin(),nf.getIron(),nf.getNiacin(),nf.getPantothenic_acid(),nf.getPhosphorus());
			log.debug("Successfully updated option.");
		} catch (DuplicateException e) {
			log.error(e.getMessage());
			throw new DuplicateException(messageProps.getProperty("error.duplicate_code"));			
		}
	}
	
	/**
	 * Permanently deletes an option from the DB.
	 * WARNING! This should rarely be done. All order history with this option must be deleted first
	 * Use with extreme caution. Deactivating an option is usually preferred. 
	 * @param itemOptionId the id of the option to be deleted
	 * @throws DeleteException if the option cannot be deleted because it has order history
	 */
	public void deleteOption(int companyid, int itemOptionId) throws DeleteException {
		log.debug("Permanently deleting option '" + itemOptionId + "' and all their related objects.");
		try {
			restaurantManagementDAO.item_option_del(companyid, itemOptionId);
		} catch (DeleteException e) {
			log.error("Could not delete due to constraints. All required related objects must be deleted first.");
			throw new DeleteException("error;" + e.getMessage() + " Please delete before proceeding.");
		}
		log.debug("Successfully deleted option.");
	}
}