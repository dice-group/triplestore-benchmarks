package org.aksw.queries.stats;

import org.aksw.simba.dataset.stats.Structuredness;

public class EndpointSize {

	public static void main(String[] args) {
		String endpoint = args[0];
		String namedGraph = null ;
		//			String endpoint = "http://linkedgeodata.org/sparql";
		//	initializeRepoConnection (endpoint);
		if(args.length==2)
			namedGraph = args[1]; 
		System.out.println("total size: "+ Structuredness.totalTriples(endpoint, namedGraph));

	}

}
