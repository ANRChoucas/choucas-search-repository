package choucas.search.tests;

import choucas.search.settings.SearchConfig;

/**
 * Tests of the Json configuration file
 * see : https://github.com/frankred/json-config-file
 * doc : https://github.com/frankred/json-config-file#readme
 * 
 * @author Eric Gouardères
 * @version 1.0
 */

public class TestConfig {
	
	public static void main(String[] args) {
		
		String relativeWebPath =".\\src\\main\\resources\\config\\config.json";  
		
		SearchConfig.load(relativeWebPath);
		
		// reading attribute values
		System.out.println(SearchConfig.getInstance().TITLE);
		System.out.println(SearchConfig.getInstance().DATA_PATH);
		
		// Changing attribute values		
		// SearchConfig.getInstance().TITLE = "titi";

		// Writing config file
		SearchConfig.getInstance().toFile(relativeWebPath);
	}
}
