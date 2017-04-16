package org.roundrobin.socket;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class CategoriesPojo {
	public static Properties categories;
	public static String absolutePath;

	public static void loadProperties(String path) throws FileNotFoundException, IOException {
		absolutePath = path;
		categories = new Properties();
		categories.load(new FileInputStream(path));
	}
	
	public static void saveProperties(){
		try {
			categories.store(new FileWriter(absolutePath),"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
