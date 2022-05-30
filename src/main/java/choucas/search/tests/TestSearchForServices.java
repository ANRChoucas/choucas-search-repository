package choucas.search.tests;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import choucas.search.settings.SearchConfig;

/**
 * Examples of requests to the discovery web service
 * Provided as is, should be revised and cleaned
 * 
 * @author Eric Gouardères
 * @version 1.0
 */

public class TestSearchForServices {
	public static void main(String[] args) {
		
		// Setting server parameters
		String configPath =".\\src\\main\\resources\\config\\config.json";  
		SearchConfig.load(configPath);
		String server = SearchConfig.getInstance().SERVER;
		
		// Setting request parameters
		String uri = "";
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = null;
		
		// Setting response parameters
		String response = "";
	
		// Example of a request specifying only the functionality 
		uri = server + "/searchRepository/Service?Functionnality=Direct_geocoding_service";
		target = client.target(uri);

		response = target.request().accept(MediaType.TEXT_PLAIN).get(String.class);
		
		System.out.println("Result of request specifying only the functionality");
		prettyPrintJson(response);
		
		// Example of a request specifying the functionality and InOut parameters  
		uri = server + "/searchRepository/Service?Functionnality=Direct_geocoding_service&Inputs=SpatialEntity&Outputs=GeographicCoordinates";
		target = client.target(uri);

		response = target.request().accept(MediaType.TEXT_PLAIN).get(String.class);
		
		System.out.println("Result of request request specifying the functionality and InOut parameters");
		prettyPrintJson(response);

		// Example of a request specifying the functionality and InOut parameters with corresponding thresholds of 0.5  
		uri = server + "/searchRepository/Service?Functionnality=Distance_calculation_service&Inputs=SourceLocation&Inputs=Impedance&Outputs=Distance&Outputs=EuclideanDistance&ThresholdFunc=0.5&ThresholdInOut=0.5";

		target = client.target(uri);

		response = target.request().accept(MediaType.TEXT_PLAIN).get(String.class);

		System.out.println("Result of request specifying the functionality and InOut parameters with corresponding thresholds of 0.5");
		prettyPrintJson(response);
		
		// Example of a request specifying the functionality, InOut parameters with corresponding thresholds of 0.5, functional/non functional Weight of 0.5
		uri = server + "/searchRepository/Service?Functionnality=Distance_calculation_service&Inputs=SourceLocation&Inputs=Impedance&Outputs=Distance&Outputs=EuclideanDistance&ThresholdFunc=0.5&ThresholdInOut=0.5&WeightingFunNFun=0.5";

		target = client.target(uri);

		response = target.request().accept(MediaType.TEXT_PLAIN).get(String.class);

		System.out.println("Result of request specifying the functionality, InOut parameters with corresponding thresholds of 0.5 and functional/non functional Weight of 0.5");
		prettyPrintJson(response);
	
		// Example of a request specifying the functionality, InOut parameters with corresponding thresholds of 0.5, functional/non functional Weight of 0.5 and objective properties weight of 1
		uri = server + "/searchRepository/Service?Functionnality=Distance_calculation_service&Inputs=SourceLocation&Inputs=Impedance&Outputs=Distance&Outputs=EuclideanDistance&ThresholdFunc=0.5&ThresholdInOut=0.5&WeightingFunNFun=0.5&WeightingObjSub=1";

		target = client.target(uri);

		response = target.request().accept(MediaType.TEXT_PLAIN).get(String.class);

		System.out.println("Result of request specifying the functionality, InOut parameters with corresponding thresholds of 0.5, functional/non functional Weight of 0.5 and objective properties weight of 1  ");
		prettyPrintJson(response);
	}
	
	private static void prettyPrintJson(String jsonString) {
		int tab = 2;
		if (jsonString.charAt(0) == '{') {
			System.out.println(new JSONObject(jsonString).toString(tab));
		}
		if (jsonString.charAt(0) == '[') {
			System.out.println(new JSONArray(jsonString).toString(tab));
		}
		
	}
}

