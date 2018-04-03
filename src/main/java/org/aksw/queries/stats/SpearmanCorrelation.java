package org.aksw.queries.stats;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.RankingAlgorithm;
import org.apache.commons.math3.stat.ranking.TiesStrategy;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
/**
 * Spearman Correlation 
 * @author Saleem
 *
 */
public class SpearmanCorrelation {

	public static void main(String[] args) throws IOException {
		//	 NaturalRanking ranking = new NaturalRanking(NaNStrategy.REMOVED);
		//      SpearmansCorrelation spearman = new SpearmansCorrelation(ranking);

		//		 double[] noVariance = new double[] {1, 2, 3, 1};
		//	        double[] values = new double[] {1, 4, 12, 16};
		//	       System.out.println(spearman.correlation(noVariance, values));
		String endpoint = args[0];
		//   String endpoint = "http://localhost:8890/sparql";
		String namedGraph = args[1] ;
		//  String namedGraph = "http://benchmark-eval.aksw.org/watdiv";
		//     String queryFile = "queries.txt" ;
		String queryFile = args[2];

		printCorrelations(endpoint,namedGraph,queryFile); // see also getCorrelations method

	}
	/**
	 * Print Spearmans correlation coefficient values
	 * @param endpoint endpoint url
	 * @param namedGraph graph name
	 * @param queryFile the queries files containing the queries or SPARQL features
	 * @throws IOException
	 */
	public static void printCorrelations(String endpoint, String namedGraph, String queryFile) throws IOException {


		//String queryString = "SELECT ?rs ?ms  WHERE { ?s <http://lsq.aksw.org/vocab#text> ?text . ?s <http://lsq.aksw.org/vocab#hasLocalExec> ?le . ?le <http://lsq.aksw.org/vocab#resultSize> ?rs . ?s <http://lsq.aksw.org/vocab#hasSpin> ?spin . ?spin a <http://spinrdf.org/sp#Select> . ?le <http://lsq.aksw.org/vocab#runTimeMs> ?ms}" ; 
		List<String> queries = getQueries(queryFile);
		for(String queryString:queries){
			Query query = QueryFactory.create(queryString); 
			if(namedGraph!=null)
			query.addGraphURI(namedGraph);
			double coefficient = getCoefficient(endpoint,query);
		//	System.out.println(coefficient);
			System.out.println("Query feature: "+query.getProjectVars().get(0)+"\tSpearmans correlation coefficient: "+coefficient);
		}

	}

	/**
	 * Get Spearmans correlation coefficient values
	 * @param endpoint endpoint url
	 * @param namedGraph graph name
	 * @param queryFile the queries files containing the queries or SPARQL features
	 * @return A map of spearsman coefficient values for each query feature
	 * @throws IOException
	 */
	public static Map<String, Double> getCorrelations(String endpoint, String namedGraph, String queryFile) throws IOException {
		//String queryString = "SELECT ?rs ?ms  WHERE { ?s <http://lsq.aksw.org/vocab#text> ?text . ?s <http://lsq.aksw.org/vocab#hasLocalExec> ?le . ?le <http://lsq.aksw.org/vocab#resultSize> ?rs . ?s <http://lsq.aksw.org/vocab#hasSpin> ?spin . ?spin a <http://spinrdf.org/sp#Select> . ?le <http://lsq.aksw.org/vocab#runTimeMs> ?ms}" ; 
		List<String> queries = getQueries(queryFile);
		Map<String, Double> correlations = new LinkedHashMap<String,Double> ();
		for(String queryString:queries){
			Query query = QueryFactory.create(queryString); 
			query.addGraphURI(namedGraph);
			double coefficient = getCoefficient(endpoint,query);
			//System.out.println(coefficient);
			correlations.put(query.getProjectVars().get(0).toString(),coefficient);
			//System.out.println(query.getProjectVars().get(0)+"\t"+coefficient);
		}
		return correlations ;
	}
	public static List<String> getQueries(String queryFile) throws IOException {
		List<String> queries = new ArrayList<String> () ; 
		File file = new File(queryFile);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			queries.add(line);
		}
		return queries;
	}
	/**
	 * Get the spearsman cofficient value for the given query feature
	 * @param endpoint endpoint containing the LSQ dataset of the benchmark queries
	 * @param query The SPARQL query to get values for the required query feature
	 * @return coefficient value
	 */
	public static double getCoefficient(String endpoint,Query query) {

		QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
		ResultSet res= qExe.execSelect();
		List<Double> features = new ArrayList<Double>(); 
		List<Double> runtimes = new ArrayList<Double>(); 
		while(res.hasNext()){
			QuerySolution solution = res.next();
			List<String> var =       res.getResultVars();
			//	System.out.println(solution);
			if(solution.get(var.get(0)).isResource())
				features.add(solution.get(var.get(0)).asLiteral().getDouble());	
			else
				if(solution.get(var.get(0)).asLiteral().toString().contains("NAN"))
					features.add(0.0);
				else
					features.add(solution.get(var.get(0)).asLiteral().getDouble());	

			runtimes.add(solution.get(var.get(1)).asLiteral().getDouble());	
		}
		NaturalRanking ranking = new NaturalRanking(NaNStrategy.REMOVED);
		SpearmansCorrelation spearman = new SpearmansCorrelation(ranking);
		//   System.out.println(features +"\n"+runtimes);
		double [] array1 = getDouble(features);
		double [] array2 = getDouble(runtimes);
		return spearman.correlation( array2,array1);
	}
	/**
	 * conver list <Double> to doubles [] 
	 * @param doubles list of doubles
	 * @return array of doubles []
	 */
	public static double[] getDouble(List<Double> doubles) {
		double[] target = new double[doubles.size()];
		for (int i = 0; i < target.length; i++) {
			target[i] = doubles.get(i).doubleValue();  // java 1.4 style
			// or:
			target[i] = doubles.get(i);                // java 1.5+ style (outboxing)
		}
		return target;
	}

}
