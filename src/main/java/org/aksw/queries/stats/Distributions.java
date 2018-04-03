package org.aksw.queries.stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
/**
 * Get the distribution of various SPARQL constructs in percentages. 
 * Requrie an input queries as file to get distributions
 * @author Saleem
 *
 */
public class Distributions {

	public static void main(String[] args) throws IOException {
		String endpoint = args[0];
		//	String endpoint = "http://localhost:8890/sparql";
		String namedGraph = args[1] ;
		//		String namedGraph = "http://benchmark-eval.aksw.org/biobench";
		//	String percentQueryFile = "queries-percent.txt" ;
		String percentQueryFile = args[2];
		long totalQueries = getTotalLSQQueries(endpoint,namedGraph);
		//     System.out.println(totalQueries);
		Map<String,Double> percentages = getPercentages(endpoint,namedGraph,percentQueryFile,totalQueries);
		//   System.out.println(percentages.keySet());
		Collection<Double> values = percentages.values();
		for(Double value:values){
			System.out.print(value+"\t");
		}
		// System.out.println("\n"+percentages);
	}
	/**
	 * Toal number of queries in the LSQ output dataset
	 * @param endpoint the endpoint containing the LSQ RDF dataset
	 * @param namedGraph Graph name
	 * @return Total unique queries
	 */
	public static long getTotalLSQQueries(String endpoint, String namedGraph) {
		String queryStr = " SELECT (count(DISTINCT ?text) as ?UQ) WHERE { ?s <http://lsq.aksw.org/vocab#text> ?text . ?s <http://lsq.aksw.org/vocab#hasSpin> ?spin . ?spin a <http://spinrdf.org/sp#Select> .}	";
		Query query = QueryFactory.create(queryStr); 
		if(namedGraph!=null)
		query.addGraphURI(namedGraph);
		QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
		ResultSet res= qExe.execSelect();
		long totalQueries = 0; 
		while(res.hasNext()){
			totalQueries = res.next().get("UQ").asLiteral().getLong();
		}
		return totalQueries;
	}
	/**
	 * Get distributions of a SPARQL clauses in percentage value
	 * @param endpoint Endpoint containing LSQ dataset
	 * @param namedGraph Graph name
	 * @param percentQueryFile The file containing SPARQL queries one per SPARQL clause
	 * @param totalQueries Total number of unique queries
	 * @return percentage-wise distributions of SPARQL clauses. 
	 * @throws IOException
	 */
	public static Map<String, Double> getPercentages(String endpoint, String namedGraph, String percentQueryFile, long totalQueries) throws IOException {
		List<String> queries = SpearmanCorrelation.getQueries(percentQueryFile);
		//	System.out.println(queries.size());
		Map<String, Double> percentages = new LinkedHashMap<String,Double> ();
		for(String queryString:queries){
			Query query = QueryFactory.create(queryString); 
			if(namedGraph!=null)
			query.addGraphURI(namedGraph);
			//	System.out.println(query);
			double percent = getPercentDist(endpoint,query,totalQueries);
			percentages.put(query.getProjectVars().get(0).toString(),percent);

		}
		return percentages;
	}
	/**
	 * Get the percentage distribution of a SPARQL clause 
	 * @param endpoint SPARQL endpoint containing LSQ file
	 * @param query the query to get the values for the specific SPARQL clause
	 * @param totalQueries Total number of quries
	 * @return percentage distribution
	 */
	public static double getPercentDist(String endpoint, Query query, long totalQueries) {
		QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
		long count = 0 ; 
		ResultSet res= qExe.execSelect();
		while(res.hasNext()){
			QuerySolution solution = res.next();
			List<String> var =       res.getResultVars();
			count =    solution.get(var.get(0)).asLiteral().getLong();
		}
		return  ((double)count*100/totalQueries);
	}
}