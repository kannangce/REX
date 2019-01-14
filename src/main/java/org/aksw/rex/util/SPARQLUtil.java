package org.aksw.rex.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.dllearner.kb.sparql.SparqlEndpoint;

/**
 * Utils containing the methods SPARQL.
 * 
 * @author kannan.r
 *
 */
public class SPARQLUtil {

	public static SparqlEndpoint getEndpoint() {
		try {
			return new SparqlEndpoint(new URL("http://localhost:8890/sparql"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
