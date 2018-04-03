package org.aksw.simba.dataset.stats;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
/**
 * Get relationship specialty of a dataset
 * @author Saleem
 *
 */
public class RelationshipSpeciality {
	public static boolean error ;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//	Kurtosis k = new Kurtosis(); 
		///	double[] values = {1,1,1,1,1,1};
		//	System.out.println(k.evaluate(values ));
		String endpoint = args[0];
		String namedGraph = null ;
		//			String endpoint = "http://linkedgeodata.org/sparql";
		//	initializeRepoConnection (endpoint);
		if(args.length==2)
			namedGraph = args[1]; 
		//String namedGraph = "http://linkedgeodata.org"; 
		System.out.println("Process started...");
		double relationSpeciality = getSRelationshipSpecialty(endpoint, namedGraph);
		System.out.println("Relationship specialty: "+relationSpeciality);
	}
	/**
	 * Get relationship specialty of the dataset
	 * @param endpoint the endpoint url containing the datset
	 * @param namedGraph graph name
	 * @return the specialty value
	 */
	public static double getSRelationshipSpecialty(String endpoint, String namedGraph) {
		Set<String> predicates = getRelationshipPredicates(endpoint, namedGraph);
		System.out.println("Total Relationship Predicates to process: "+ predicates.size());
		long datasetSize = Structuredness.totalTriples(endpoint, namedGraph);
		System.out.println("Total triples: "+ datasetSize);
		long subjects = Structuredness.totalSubjects(endpoint, namedGraph);
		System.out.println("Total resources: "+ subjects);
		Kurtosis kurt = new Kurtosis(); 
		double relationshipSpecialty = 0 ;
		int i = 1;
		for (String predicate:predicates){
			error = false ;
			double [] occurences = getOccurences(predicate,endpoint,namedGraph,subjects);
			//	System.out.println(error);
			if(error==false){
				double kurtosis = kurt.evaluate(occurences);
				//	System.out.println("Kurtosis: " +kurtosis);
				long tpSize = getPredicateSize(predicate,endpoint,namedGraph);
				//	System.out.println("T.p Size: " +tpSize);
				relationshipSpecialty = relationshipSpecialty + (tpSize*kurtosis/datasetSize);
				System.out.println(i+": "+predicate+"\t Relationship Specialty so far: "+(relationshipSpecialty));
			}i++;
		}
		return relationshipSpecialty;
	}
	/**
	 * Number of triples for a given predicate
	 * @param predicate predicate
	 * @param endpoint the endpoint url of the dataset
	 * @param namedGraph graph name
	 * @return #triples 
	 */
	public static long getPredicateSize(String predicate, String endpoint, String namedGraph)  {
		long count = 0; 
		String queryString ="";
		try{
			if(namedGraph ==null)
				queryString = "SELECT (Count(*) as ?total) \n"
						+ "			WHERE { \n"
						+ "            ?s <"+predicate+"> ?o"
						+ "           }" ;
			else
				queryString = "SELECT  (Count(*) as ?total) From <"+ namedGraph+"> \n"
						+ "			WHERE { \n"
						+ "            ?s <"+predicate+"> ?o"
						+ "           }" ;
			//System.out.println(queryString);
			Query query = QueryFactory.create(queryString); 
			QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
			ResultSet res= qExe.execSelect();
			while(res.hasNext())
			{
				count = Long.parseLong(res.next().get("total").asLiteral().getString());
			}
		}catch(Exception e){return 0 ;}
		return count;
	}
	/** 
	 * Get occurences of the given predicate pertaining to the subject
	 * @param predicate predicate
	 * @param endpoint endpoint containing the dataset
	 * @param namedGraph named graph
	 * @param subjects number of subjects
	 * @return
	 */
	public static double[] getOccurences(String predicate, String endpoint, String namedGraph,long subjects) {

		double [] occurences = new double[(int) subjects+1];
		//System.out.println("Distribution sizue should be :" + occurences.length);
		String queryString ;   try{
			if(namedGraph ==null)
				queryString = "SELECT (count(?o) as ?occ) WHERE { ?res <"+predicate+"> ?o . } Group by ?res" ;
			else
				queryString = "SELECT (count(?o) as ?occ) From <"+ namedGraph+">  WHERE { ?res <"+predicate+"> ?o .  } Group by ?res  " ;

			//	System.out.println(predicate+" : "+queryString);
			Query query = QueryFactory.create(queryString); 
			QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );

			ResultSet res= qExe.execSelect();
			int i = 0 ;
			while(res.hasNext()){
				occurences[i] = 	res.next().get("occ").asLiteral().getDouble();
				i++;
			}
			if (i==0)
				occurences[0] = 1;
			//	System.out.println("Distribution is ready");
		}catch (Exception ex){System.out.println(ex);error=true;}
		return occurences ;
	}
	/**
	 * get the relationship predicates in a given dataset
	 * @param endpoint endpoint url of the dataset
	 * @param namedGraph graph name
	 * @return set of relationship predicate
	 */
	public static Set<String> getRelationshipPredicates(String endpoint,String namedGraph)  {
		Set<String> predicates =new HashSet<String>() ;
		String queryString ;
		if(namedGraph ==null)
			queryString = "SELECT DISTINCT ?p Where {?s ?p ?o . FILTER isIRI(?o) } " ;
		else
			queryString = "SELECT DISTINCT ?p From <"+ namedGraph+">  WHERE {?s ?p ?o . FILTER isIRI(?o) }" ;
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
		QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
		ResultSet res= qExe.execSelect();
		while(res.hasNext())
			predicates.add(res.next().get("p").toString());		
		return predicates;
	}

}
