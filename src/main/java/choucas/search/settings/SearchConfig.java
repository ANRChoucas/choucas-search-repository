package choucas.search.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Management of the Json configuration file
 * see : https://github.com/frankred/json-config-file
 * doc : https://github.com/frankred/json-config-file#readme
 * 
 * @author Eric Gouardères
 * @version 1.0
 */

public class SearchConfig {

	// Définir ici les attributs
	public String TITLE;
	public String DATA_PATH;
	public String SERVER;
	

	//public ArrayList<String> NAMES;

	public SearchConfig() {
		// Initialiser ici les attributs
		this.TITLE = "Choucas service search engine";
		this.DATA_PATH = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ROOT\\WEB-INF\\data\\";
	}

	// DON'T TOUCH THE FOLLOWING CODE
	private static SearchConfig instance;

	public static SearchConfig getInstance() {
		if (instance == null) {
			instance = fromDefaults();
		}
		return instance;
	}

	public static void load(File file) {
		instance = fromFile(file);

		// no config file found
		if (instance == null) {
			instance = fromDefaults();
		}
	}

	public static void load(String file) {
		load(new File(file));
	}

	private static SearchConfig fromDefaults() {
		SearchConfig config = new SearchConfig();
		return config;
	}

	public void toFile(String file) {
		toFile(new File(file));
	}

	public void toFile(File file) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonConfig = gson.toJson(this);
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write(jsonConfig);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static SearchConfig fromFile(File configFile) {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
			return gson.fromJson(reader, SearchConfig.class);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
}
