
package org.aksw.simba.dataset.stats ;
import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;


/**
 * Calculate the structuredness or coherence of a dataset as defined in Duan et al. paper titled "Apples and Oranges: A Comparison of
   RDF Benchmarks and Real RDF Datasets". The structuredness is measured in interval [0,1]  with values close to 0 corresponding to low structuredness, and
   1 corresponding to perfect structuredness. The paper concluded that synthetic datasets have high structurenes as compared to real datasets.
 * @author Saleem
 *
 */
public class Structuredness {


	public static void main(String[] args)  {

		String endpoint = args[0];
		String namedGraph = null ;
    	if(args.length==2)
		 namedGraph = args[1]; 
	
		double coherence = getStructurednessValue(endpoint, namedGraph);
		System.out.println("Structuredness: " + coherence);
		System.out.println("\n Subject\tPredicates\tObjects\tTriples\tStructuredness \n " + totalSubjects(endpoint,namedGraph)+"\t"+totalPredicates(endpoint,namedGraph)+"\t"+totalObjeects(endpoint,namedGraph)+"\t"+totalTriples(endpoint,namedGraph)+"\t"+ coherence);

	}

	/**
	 * Get the structuredness/coherence value [0,1] of a dataset
	 * @param endpointUrl SPARQL endpoint URL
	 * @param namedGraph Named Graph of dataset. Can be null, in that case all named graphs will be considered
	 * @param types 
	 * @return structuredness Structuredness or coherence value
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static double getStructurednessValue(String endpointUrl,
			String namedGraph)  {
		
		Set<String> types = getRDFTypes(namedGraph,endpointUrl);
		System.out.println("Total rdf:types: " + types.size());
		double weightedDenomSum = getTypesWeightedDenomSum(types,namedGraph,endpointUrl);
		double structuredness = 0;
		long count = 1;
		for(String type:types)
		{
			long occurenceSum = 0;
			Set<String> typePredicates = getTypePredicates(type,namedGraph,endpointUrl);
			long typeInstancesSize = getTypeInstancesSize(type,namedGraph,endpointUrl);
			//System.out.println(typeInstancesSize);
			//System.out.println(type+" predicates: "+typePredicates);
			//System.out.println(type+" : "+typeInstancesSize+" x " + typePredicates.size());
			for (String predicate:typePredicates)
			{
				long predicateOccurences = getOccurences(predicate,type,namedGraph,endpointUrl);
				occurenceSum = (occurenceSum + predicateOccurences);
				//System.out.println(predicate+ " occurences: "+predicateOccurences);
				//System.out.println(occurenceSum);
			}

			double denom = typePredicates.size()*typeInstancesSize;
			if(typePredicates.size()==0)
				denom = 1;
			//System.out.println("Occurence sum  = " + occurenceSum);
			//System.out.println("Denom = " + denom);
			double coverage = occurenceSum/denom;
			System.out.println("\n"+count+ " : Type: " + type );
			System.out.println("Coverage : "+ coverage);
			double weightedCoverage = (typePredicates.size()+ typeInstancesSize) / weightedDenomSum;
			System.out.println("Weighted Coverage : "+ weightedCoverage);
			structuredness = (structuredness + (coverage*weightedCoverage));
			count++;
		}

		return structuredness;
	}
	/**
	 * Get the denominator of weighted sum all types. Please see Duan et. all paper apple oranges
	 * @param types Set of rdf:types
	 * @param namedGraph Named graph
	 * @return sum Sum of weighted denominator
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static double getTypesWeightedDenomSum(Set<String> types, String namedGraph,String endpoint) {
		double sum = 0 ; 
		for(String type:types)
		{
			long typeInstancesSize = getTypeInstancesSize(type,namedGraph, endpoint);
			long typePredicatesSize = getTypePredicates(type,namedGraph,endpoint).size();
			sum = sum + typeInstancesSize + typePredicatesSize;
		}
		return sum;
	}
	/**
	 * Get occurences of a predicate within a type
	 * @param predicate Predicate
	 * @param type Type
	 * @param namedGraph Named Graph
	 * @return predicateOccurences Predicate occurence value
	 * @throws NumberFormatException
	 * @throws QueryEvaluationException
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 */
	public static long getOccurences(String predicate, String type, String namedGraph, String endpoint)  {
		long predicateOccurences = 0  ;
		String queryString ;
		if(namedGraph ==null)
			queryString = "SELECT (Count(Distinct ?s) as ?occurences) \n"
					+ "			WHERE { \n"
					+ "            ?s a <"+type+"> . "
					+ "            ?s <"+predicate+"> ?o"
					+ "           }" ;
		else
			queryString = "SELECT (Count(Distinct ?s) as ?occurences) From <"+ namedGraph+"> \n"
					+ "			WHERE { \n"
					+ "            ?s a <"+type+"> . "
					+ "            ?s <"+predicate+"> ?o"
					+ "           }" ;
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
		while(res.hasNext())
		{
			predicateOccurences = Long.parseLong(res.next().get("occurences").asLiteral().getString());
		}
		return predicateOccurences;

	}
	/**
	 * Get the number of distinct instances of a specfici type
	 * @param type Type or class name
	 * @param namedGraph Named graph
	 * @return typeInstancesSize No of instances of type 
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static long getTypeInstancesSize(String type, String namedGraph,String endpoint)  {
		long typeInstancesSize =0;
		String queryString ;
		if(namedGraph ==null)
			queryString = "SELECT (Count(DISTINCT ?s)  as ?cnt ) \n"
					+ "			WHERE { \n"
					+ "            ?s a <"+type+"> . "
					+ "            ?s ?p ?o"
					+ "           }" ;
		else
			queryString = "SELECT (Count(DISTINCT ?s)  as ?cnt) From <"+ namedGraph+"> \n"
					+ "			WHERE { \n"
					+ "            ?s a <"+type+"> . "
					+ "            ?s ?p ?o"
					+ "           }" ;
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
		while(res.hasNext())
		{
			typeInstancesSize =  Long.parseLong(res.next().get("cnt").asLiteral().getString());
		}
		return typeInstancesSize;
	}
	/**
	 * Get all distinct predicates of a specific type
	 * @param type Type of class
	 * @param namedGraph Named Graph can be null
	 * @return typePredicates Set of predicates of type 
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static Set<String> getTypePredicates(String type, String namedGraph,String endpoint)  {
		Set<String> typePredicates =new HashSet<String>() ;
		String queryString ;
		if(namedGraph ==null)
			queryString = "SELECT DISTINCT ?typePred \n"
					+ "			WHERE { \n"
					+ "            ?s a <"+type+"> . "
					+ "            ?s ?typePred ?o"
					+ "           }" ;
		else
			queryString = "SELECT DISTINCT ?typePred From <"+ namedGraph+"> \n"
					+ "			WHERE { \n"
					+ "            ?s a <"+type+"> . "
					+ "            ?s ?typePred ?o"
					+ "           }" ;
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
		while(res.hasNext())
		{
			String predicate = res.next().get("typePred").toString();
			if (!predicate.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"))
				typePredicates.add(predicate);
		}
		return typePredicates;
	}
	
	/**
	 *  Get distinct set of rdf:type
	 * @param namedGraph Named Graph of dataset can be null in that case all namedgraphs will be considered
	 * @param endpoint 
	 * @return types Set of rdf:types
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static Set<String> getRDFTypes(String namedGraph, String endpoint) {
		Set<String> types =new HashSet<String>() ;
		String queryString ="";
		if(namedGraph ==null)
			queryString = "SELECT DISTINCT ?type  \n"
					+ "			WHERE { \n"
					+ "            ?s a ?type"
					+ "           }" ;
		else
			queryString = "SELECT DISTINCT ?type From <"+ namedGraph+"> \n"
					+ "			WHERE { \n"
					+ "            ?s a ?type"
					+ "           }" ;
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
		while(res.hasNext())
		{
			types.add(res.next().get("type").toString());
		}
		return types;
	}
	public static long totalSubjects(String endpoint, String namedGraph)  {
		long count = 0; 
		String queryString ="";
		if(namedGraph ==null)
			queryString = "SELECT (Count(DISTINCT ?s) as ?total) \n"
					+ "			WHERE { \n"
					+ "            ?s ?p ?o"
					+ "           }" ;
		else
			queryString = "SELECT  (Count(DISTINCT ?s) as ?total) From <"+ namedGraph+"> \n"
					+ "			WHERE { \n"
					+ "            ?s ?p ?o"
					+ "           }" ;
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
		while(res.hasNext())
		{
			count = Long.parseLong(res.next().get("total").asLiteral().getString());
		}
		return count;
	}
	public static long totalTriples(String endpoint, String namedGraph)  {
		long count = 0; 
		String queryString ="";
		if(namedGraph ==null)
			queryString = "SELECT (Count(*) as ?total) \n"
					+ "			WHERE { \n"
					+ "            ?s ?p ?o"
					+ "           }" ;
		else
			queryString = "SELECT  (Count(*) as ?total) From <"+ namedGraph+"> \n"
					+ "			WHERE { \n"
					+ "            ?s ?p ?o"
					+ "           }" ;
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
		while(res.hasNext())
		{
			count = Long.parseLong(res.next().get("total").asLiteral().getString());
		}
		return count;
	}
	public static long totalPredicates(String endpoint, String namedGraph)  {
		long count = 0; 
		String queryString ="";
		if(namedGraph ==null)
			queryString = "SELECT (Count(DISTINCT ?p) as ?total) \n"
					+ "			WHERE { \n"
					+ "            ?s ?p ?o"
					+ "           }" ;
		else
			queryString = "SELECT  (Count(DISTINCT ?p) as ?total) From <"+ namedGraph+"> \n"
					+ "			WHERE { \n"
					+ "            ?s ?p ?o"
					+ "           }" ;
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
		while(res.hasNext())
		{
			count = Long.parseLong(res.next().get("total").asLiteral().getString());
		}
		return count;
	}
	public static long totalObjeects(String endpoint, String namedGraph)  {
		long count = 0; 
		String queryString ="";
		if(namedGraph ==null)
			queryString = "SELECT (Count(DISTINCT ?o) as ?total) \n"
					+ "			WHERE { \n"
					+ "            ?s ?p ?o"
					+ "           }" ;
		else
			queryString = "SELECT  (Count(DISTINCT ?o) as ?total) From <"+ namedGraph+"> \n"
					+ "			WHERE { \n"
					+ "            ?s ?p ?o"
					+ "           }" ;
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
		while(res.hasNext())
		{
			count = Long.parseLong(res.next().get("total").asLiteral().getString());
		}
		return count;
	}
}