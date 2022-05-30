package choucas.search.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Functional_matching_calculation.Functionality_fileAnalyzer_Calculation;
import Functional_matching_calculation.InOut_fileAnalyzer_Calculation;
import QoS_Calculation.QoS_FileAnalyzer_Calculation;
import Recommandation_calculation.ScoreRecommandation;

/**
 * The landing page of the search for service/processes engine
 * 
 * @author Eric Gouardères
 * @version 1.0
 */

@Path("/")
public class SearchLanding {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String Response() {
		String result = "Welcome to the landing page of the search for service/processes engine";
		return(result);
	}

}