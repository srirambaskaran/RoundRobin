package org.roundrobin.socket;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.EncodeException;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdminManager {
	public static JSONObject doOperation(String operation, HttpServletRequest request){
		if(operation.equals("getCategories")){
			return getCategories(request);
		}else if(operation.equals("createNewCategory")){
			return createNewCategory(request);
		}else if(operation.equals("saveList")){
			return saveList(request);
		}else if(operation.equals("getList")){
			return getList(request);
		}else if(operation.equals("next")){
			return next(request);
		}else if(operation.equals("prev")){
			return prev(request);
		}else if(operation.equals("deleteEntry")){
			return deleteEntry(request);
		}
		return null;
	}
	
	private static JSONObject deleteEntry(HttpServletRequest request){
		String category = request.getParameter("category");
		String entry = request.getParameter("entry");
		String categoryValues = CategoriesPojo.categories.getProperty("category."+category+".list");
		categoryValues = categoryValues.replace(","+entry, "");
		categoryValues = categoryValues.replace(entry+",", "");
		CategoriesPojo.categories.setProperty("category."+category+".list",categoryValues);
		CategoriesPojo.categories.setProperty("category."+category+".selected","0");
		CategoriesPojo.saveProperties();
		JSONObject message = new JSONObject();
		message.put("status", true);
		message.put("message", "Added Category List");
		String[] listValues = categoryValues.split(",");
		JSONArray categories = new JSONArray();
		for(String value : listValues){
			categories.put(value);
		}
		message.put("list", categories);
		message.put("selected", "0");
		return message;
		
	}
	private static JSONObject getCategories(HttpServletRequest request) {
		String categoriesLine = CategoriesPojo.categories.getProperty("categories");
		JSONObject message = new JSONObject();
		JSONArray categoriesArray = new JSONArray();
		String[] tokens = categoriesLine.split(",");
		for(String token: tokens){
			categoriesArray.put(token);
		}
		message.put("status", true);
		message.put("message", "Fetched Categories");
		message.put("categories",categoriesArray);
		return message;
	}
	
	private static JSONObject createNewCategory(HttpServletRequest request){
		String category = request.getParameter("category");
		String categoriesLine = CategoriesPojo.categories.getProperty("categories");
		if(categoriesLine.length()==0){
			categoriesLine = category;
		}else{
			categoriesLine += "," + category;
		}
		
		CategoriesPojo.categories.setProperty("categories", categoriesLine);
		CategoriesPojo.categories.setProperty("category."+category+".selected", "0");
		CategoriesPojo.categories.setProperty("category."+category+".list", "");
		CategoriesPojo.saveProperties();
		JSONObject message = new JSONObject();
		message.put("status", true);
		message.put("message", "Added Category");
		JSONArray categoriesArray = new JSONArray();
		String[] tokens = categoriesLine.split(",");
		for(String token: tokens){
			categoriesArray.put(token);
		}
		message.put("categories", categoriesArray);
		return message;
	}
	
	private static JSONObject saveList(HttpServletRequest request){
		String category = request.getParameter("category");
		String entry = request.getParameter("entry");
		String categoryValues = CategoriesPojo.categories.getProperty("category."+category+".list");
		if(categoryValues.length()==0){
			categoryValues = entry;
		}else{
			categoryValues += "," + entry;
		}
		CategoriesPojo.categories.setProperty("category."+category+".list", categoryValues);
		CategoriesPojo.saveProperties();
		JSONObject message = new JSONObject();
		message.put("status", true);
		message.put("message", "Added Category List");
		String[] listValues = categoryValues.split(",");
		JSONArray categories = new JSONArray();
		for(String value : listValues){
			categories.put(value);
		}
		message.put("list", categories);
		message.put("selected", CategoriesPojo.categories.getProperty("category."+category+".selected"));
		return message;
	}
	
	private static JSONObject getList(HttpServletRequest request){
		String category = request.getParameter("category");
		if(category.equals("Select")){
			JSONObject message = new JSONObject();
			message.put("status", false);
			message.put("message", "Select a category");
			return message;
		}
		String categoryValues = CategoriesPojo.categories.getProperty("category."+category+".list");
		String[] listValues = categoryValues.split(",");
		JSONArray categories = new JSONArray();
		for(String value : listValues){
			categories.put(value);
		}
		JSONObject message = new JSONObject();
		message.put("status", true);
		message.put("list",categories);
		message.put("selected",CategoriesPojo.categories.getProperty("category."+category+".selected"));
		message.put("message", "Added Category List");
		return message;
	}

	public static JSONObject getCurrentSelected() {
		JSONObject selected = new JSONObject();
		String[] categories = CategoriesPojo.categories.getProperty("categories").split(",");
		JSONArray categoriesArray = new JSONArray();
		for(String category: categories){
			int selectedIndex = Integer.parseInt(CategoriesPojo.categories.getProperty("category."+category+".selected"));
			String[] list = CategoriesPojo.categories.getProperty("category."+category+".list").split(",");
			selected.put(category,list[selectedIndex]);
			categoriesArray.put(category);
		}
		selected.put("status", true);
		selected.put("categories", categoriesArray);
		selected.put("message", "Current Selected");
		return selected;
	}
	public static JSONObject next(HttpServletRequest request){
		String category = request.getParameter("category");
		String categoryValues = CategoriesPojo.categories.getProperty("category."+category+".list");
		String[] listValues = categoryValues.split(",");
		int selected = Integer.parseInt(CategoriesPojo.categories.getProperty("category."+category+".selected"));
		return select(request, (selected+1)%listValues.length);
	}
	public static JSONObject prev(HttpServletRequest request){
		String category = request.getParameter("category");
		String categoryValues = CategoriesPojo.categories.getProperty("category."+category+".list");
		String[] listValues = categoryValues.split(",");
		int selected = Integer.parseInt(CategoriesPojo.categories.getProperty("category."+category+".selected"));
		return select(request, selected == 0 ? listValues.length-1 : selected-1);
	}
	public static JSONObject select(HttpServletRequest request, int selected){
		JSONObject message = new JSONObject();
		String category = request.getParameter("category");
		CategoriesPojo.categories.put("category."+category+".selected",selected+"");
		CategoriesPojo.saveProperties();
		try {
			UpdateSocket.sendMessageToAll();
		} catch (IOException | EncodeException e) {
			e.printStackTrace();
		}
		String categoryValues = CategoriesPojo.categories.getProperty("category."+category+".list");
		String[] listValues = categoryValues.split(",");
		JSONArray categories = new JSONArray();
		for(String value : listValues){
			categories.put(value);
		}
		message.put("status", true);
		message.put("list",categories);
		message.put("selected",selected+"");
		message.put("message", "Added Category List");
		return message;
	}
	
}
