package org.aksw.simba.dataset.cli;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.aksw.queries.stats.Distributions;
import org.aksw.queries.stats.Diversity;
import org.aksw.queries.stats.SpearmanCorrelation;
import org.aksw.simba.dataset.stats.RelationshipSpeciality;
import org.aksw.simba.dataset.stats.Structuredness;
/**
 * The CLI for benchmark utility
 * @author Saleem
 *
 */
public class Cli {

	public static void main(String[] args) throws IOException {
		//String endpoint = args[0];
		//	String endpoint = "http://localhost:8890/sparql";
		//	initializeRepoConnection (endpoint);
		//String namedGraph = args[1]; 
		// benchmark-util  -m <measure> -e <endpoint> -g <graph> -q <queriesFile>
		//System.out.println(args.length);
		List<String> arguments = getList(args);
		if(!(args.length%2==0) || args.length==0){
			printFormat();
		}
		if (!arguments.contains("-m")||!arguments.contains("-e")){
			System.out.println("The arguments -m <measure> and -e <endpoints> are mendatory");
			printFormat();
		}
		else
		{
			String measure = arguments.get(arguments.indexOf("-m")+1) ; 
			String endpoint = arguments.get(arguments.indexOf("-e")+1) ; 
			String namedGraph = null ;
			if(arguments.contains("-g"))
				namedGraph = arguments.get(arguments.indexOf("-g")+1) ;

			if(measure.equals("structuredness"))
				printStructuredness(arguments,endpoint,namedGraph);
			else if(measure.equals("specialty"))
				printSpecialty(arguments,endpoint,namedGraph);
			else if(measure.equals("diversity")){
				if (!arguments.contains("-q")){
					System.out.println("The arguments -q <queryFile> is mendatory with -m diversity");
					printFormat();
				}
				String featureQueryFile = arguments.get(arguments.indexOf("-q")+1) ; 
				printDiversityScore(endpoint,namedGraph,featureQueryFile);

			}
			else if(measure.equals("percentage")){
				if (!arguments.contains("-q")){
					System.out.println("The arguments -q <queryFile> is mendatory with -m percentage");
					printFormat();
				}
				String featureQueryFile = arguments.get(arguments.indexOf("-q")+1) ; 
				printPercentages(endpoint,namedGraph,featureQueryFile);

			}
			else if(measure.equals("correlation")){
				if (!arguments.contains("-q")){
					System.out.println("The arguments -q <queryFile> is mendatory with -m correlation");
					printFormat();
				}
				String featureQueryFile = arguments.get(arguments.indexOf("-q")+1) ; 
				printCorrelation(endpoint,namedGraph,featureQueryFile);

			}
			else
				printFormat();

		}



	}


	private static void printCorrelation(String endpoint, String namedGraph, String queryFile) throws IOException {
		SpearmanCorrelation.printCorrelations(endpoint,namedGraph,queryFile);

	}


	private static void printPercentages(String endpoint, String namedGraph, String percentQueryFile) throws IOException {
		long totalQueries = Distributions.getTotalLSQQueries(endpoint,namedGraph);
		//     System.out.println(totalQueries);
		Map<String,Double> percentages = Distributions.getPercentages(endpoint,namedGraph,percentQueryFile,totalQueries);
		//   System.out.println(percentages.keySet());
		//		Collection<Double> values = percentages.values();
		//		for(Double value:values){
		//			System.out.print(value+"\t");
		//		}
		System.out.println("\nPercentages are: \n"+percentages);

	}


	private static void printDiversityScore(String endpoint, String namedGraph, String featureQueryFile) throws IOException {
		double dScore = Diversity.getDiversityScore(endpoint,namedGraph,featureQueryFile);
		System.out.println("--------------\nOverall Diversity Score: "+ dScore);

	}


	private static void printSpecialty(List<String> arguments, String endpoint, String namedGraph) {
		System.out.println("Calculating Dataset relationship specialty...");
		double relationSpeciality = RelationshipSpeciality.getSRelationshipSpecialty(endpoint, namedGraph);
		System.out.println("--------------\nRelationship specialty: "+relationSpeciality);

	}


	private static void printStructuredness(List<String> arguments,String endpoint, String namedGraph) {
		System.out.println("Creating dataset structuredness...");
		double coherence = Structuredness.getStructurednessValue(endpoint, namedGraph);
		System.out.println("--------------\nStructuredness: " + coherence);

	}


	private static List<String> getList(String[] args) {
		List<String> arguments = new ArrayList<String>();
		for (int i = 0 ; i<args.length;i++)
			arguments.add(args[i]);
		return arguments;
	}



	private static void printFormat() {
		System.out.println("Invalid arguments. The correct format is \n\n java -jar benchmark-util.jar  -m <measure> -e <endpoint> -g <graph> -q <queriesFile> "
				+ "\n\nwhere \nmeasure = structuredness, specialty, diversity, correlation, percentage"
				+ "\nendpoint = endpoint url"
				+ "\ngraph = graph name (optional)"
				+ "\nqueriesFile = queries file (required for diversity,correlation, and percentanges of SPARQL clauses used in queries)");
		System.exit(0);

	}

}
