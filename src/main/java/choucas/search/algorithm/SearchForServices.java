package choucas.search.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import ConfigDataPath.DataPath;
import Functional_matching_calculation.Functionality_fileAnalyzer_Calculation;
import Functional_matching_calculation.InOut_fileAnalyzer_Calculation;
import QoS_Calculation.QoS_FileAnalyzer_Calculation;
import Recommandation_calculation.ScoreRecommandation;
import choucas.search.settings.SearchConfig;

/**
 * A draft example of class containing a method to define the discovery web service derived from Semantic-GWS-discovery project
 * Provided as is, should be revised and cleaned
 * 
 * @author Eric Gouardères
 * @version 1.0
 */

//Definition of the path to the resource and query parameters. 
//@Path("/Service")
@Path("/Service")
public class SearchForServices {

	// A method that allows the resource to be retrieved (for service
	// discovery) via query parameters. These parameters will be given using the
	// @QueryParam annotations.
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String Discovery(@QueryParam("Functionnality") String RequestFunctionnality,
			@QueryParam("Inputs") List<String> Inputs, @QueryParam("Outputs") List<String> Outputs,
			@DefaultValue("1.0") @QueryParam("ThresholdFunc") float ThresholdFunc,
			@DefaultValue("1.0") @QueryParam("ThresholdInOut") float ThresholdInOut,
			@DefaultValue("0.5") @QueryParam("WeightingObjSub") float WeightingObjSub,
			@DefaultValue("0.0") @QueryParam("WeightingFunNFun") float WeightingFunNFun)
			throws ParserConfigurationException, SAXException, IOException {

		List<List<String>> list_sim_functionality_accepted = new ArrayList<List<String>>();
		List<List<String>> list_sim_InOuts_accepted = new ArrayList<List<String>>();
		float simNonFunctional = 0;
		float simGlobalFANDNF = 0;
		float simFunctional = 0;
		boolean Simok = false;
		JSONObject jsonResult = new JSONObject();
		JSONArray serviceList = new JSONArray();

		// Setting Data path
		String configPath = "..\\..\\config\\config.json";
		SearchConfig.load(configPath);
		DataPath.setPath(SearchConfig.getInstance().DATA_PATH);

		// Score calculation
		// Testing parameters and setting default value if needed
		if (RequestFunctionnality == null) {
			jsonResult.put("Functionality", "Not provided");
			} 
		else {
			jsonResult.put("Functionality", RequestFunctionnality);

			// Matching score calculation of the functionality
			List<List<String>> list_sim_fonctionnality = new ArrayList<List<String>>();

			list_sim_fonctionnality = Functionality_fileAnalyzer_Calculation
					.CalculSimFunctionnality(RequestFunctionnality);

			for (List<String> elem : list_sim_fonctionnality) {

				if (Float.valueOf(elem.get(1)) >= ThresholdFunc) {
					list_sim_functionality_accepted.add(elem);
					Simok = true;
					addService1(serviceList, elem.get(2), elem.get(0), Float.valueOf(elem.get(1)));
				}

			}
			
			// updating service list in result
			jsonResult.put("Service list", serviceList);

			if (Simok) {
				Simok = false;
				
				// Score calculation
				// Testing parameters and setting default value if needed
				if (!Inputs.isEmpty() || !Outputs.isEmpty()) {
					
					// Matching score calculation of the Inputs/Outputs
					simFunctional = 0;
					serviceList = new JSONArray();

					for (List<String> elem : list_sim_functionality_accepted) {

						List<String> resPartiel = InOut_fileAnalyzer_Calculation.RequestDiscovery(elem.get(2), Inputs, Outputs);
						simFunctional = Float.valueOf(resPartiel.get(3));
						if (simFunctional >= ThresholdInOut) {
							list_sim_InOuts_accepted.add(resPartiel);
							Simok = true;
							addService1(serviceList, elem.get(2), elem.get(0), Float.valueOf(elem.get(1)), simFunctional);
						}
					}

					// updating service list in result
					jsonResult.put("Service list", serviceList);

					if (Simok) {

						// Final recommendation score calculation (Including functionnal and non functionnal scores)
						// Testing parameters and setting default value if needed
						if (WeightingFunNFun != 0.0f) {
							
							serviceList = new JSONArray();

							for (List<String> elem : list_sim_InOuts_accepted) {
								simFunctional = Float.valueOf(elem.get(3));
								simNonFunctional = QoS_FileAnalyzer_Calculation.NonFunScore(elem.get(0), WeightingObjSub);
								simGlobalFANDNF = ScoreRecommandation.RecScore(simFunctional, simNonFunctional, WeightingFunNFun);
								addService1(serviceList, elem.get(0), "not available", simFunctional, simNonFunctional, simGlobalFANDNF);

							}
							
							// updating service list in result
							jsonResult.put("Service list", serviceList);
						}
					}
				}
			}
		}

		return (jsonResult.toString());
	}

	private void addService1(JSONArray serviceList, String Name, String functionality, Float... scores) {
		JSONObject serviceDesc = new JSONObject();
		serviceDesc.put("Service name", Name);
		serviceDesc.put("Service functionality", functionality);
		switch(scores.length) {
		case 3: {
			serviceDesc.put("Service Functional score (InOut)", scores[0]);
			serviceDesc.put("Service Non Functional score", scores[1]);
			serviceDesc.put("Service Recommendation score", scores[2]);
	        break;
		}
		case 2: {
			serviceDesc.put("Service Functional score (Functionality)", scores[0]);
			serviceDesc.put("Service Functional score (InOut)", scores[1]);
	        break;
		}
		case 1: serviceDesc.put("Service Functional score (Functionality):", scores[0]);
		        break;
		}
		serviceList.put(serviceDesc);
	}

}