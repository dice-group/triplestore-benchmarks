package org.aksw.queries.stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.aksw.simba.dataset.stats.Structuredness;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
/**
 * Get the overall diversity score of the benchmark queries
 * @author Saleem
 *
 */
public class Diversity {

	public static void main(String[] args) throws IOException {
		//String endpoint = args[0];
		  String endpoint = "http://localhost:8890/sparql";
		//String namedGraph = args[1] ;
		 String namedGraph = null;
		 String featureQueryFile = "queries-diversity.txt" ;
		//String featureQueryFile = args[2];
		//     System.out.println(totalQueries);
		double dScore = getDiversityScore(endpoint,namedGraph,featureQueryFile);
		System.out.println("\nDiversity Score: "+ dScore);
	}
	// System.out.println("\n"+percentages);
	/**
	 * The overall diversity score of the benchmark queries against a set of query features
	 * @param endpoint SPARQL endpoint containing the LSQ dataset of the benchmark queries
	 * @param namedGraph Graph
	 * @param featureQueryFile The SPARQL queries file containing the important SPARQL query features. 
	 * @return the diversity score
	 * @throws IOException
	 */
	public static double getDiversityScore(String endpoint, String namedGraph, String featureQueryFile) throws IOException {
		List<String> queries = SpearmanCorrelation.getQueries(featureQueryFile);
		StandardDeviation sd = new  StandardDeviation();
		Mean mn = new Mean();
		double diversity = 0 ;
		int countFeatures = 0 ;
		for(String queryString:queries){
			Query query = QueryFactory.create(queryString); 
			if(namedGraph!=null)
			query.addGraphURI(namedGraph);
		//	System.out.println(query);
			double [] values = getFeatureValues(endpoint,query);
			double dev = sd.evaluate(values);
			//	System.out.println("S.D: " + dev);
			double mean = mn.evaluate(values);
			//	System.out.println("Mean : " + mean);
			double coeffVariance = dev/mean  ;    
			System.out.println(coeffVariance+"\t");
			diversity = diversity + coeffVariance;
			countFeatures++;

		}
		return ((double) diversity/countFeatures);

	}
	/**
	 * Get values of the required feature for all queries in the benchmark
	 * @param endpoint The LSQ endpoint of the Benchmark 
	 * @param query the SPARQL query to get values for a specific feature
	 * @return An array of values
	 */
	public static double[] getFeatureValues(String endpoint, Query query) {
		List<Double> lstValues = new ArrayList<Double> ();
		QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
		ResultSet res= qExe.execSelect();
		System.out.print("Query feature: "+ query.getProjectVars().get(0)+ "\t Diversity: ");
		while(res.hasNext()){
			QuerySolution solution = res.next();
			List<String> var =       res.getResultVars();
			double val = solution.get(var.get(0)).asLiteral().getDouble();
			lstValues.add(val);	
			//  System.out.print(val+"\t");

		}
		// System.out.print(lstValues);
		return  SpearmanCorrelation.getDouble(lstValues);
	}
}


