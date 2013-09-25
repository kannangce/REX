/**
 * 
 */
package org.aksw.rex.consistency;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dllearner.algorithms.DisjointClassesLearner;
import org.dllearner.core.owl.Axiom;
import org.dllearner.core.owl.Individual;
import org.dllearner.core.owl.NamedClass;
import org.dllearner.kb.SparqlEndpointKS;
import org.dllearner.kb.sparql.SparqlEndpoint;
import org.dllearner.reasoning.SPARQLReasoner;

import com.hp.hpl.jena.graph.Triple;

/**
 * @author Lorenz Buehmann
 *
 */
public class ConsistencyCheckerImpl implements ConsistencyChecker{
	
	private double accuracyThreshold = 0.6;
	private SparqlEndpoint endpoint; 
	private SPARQLReasoner reasoner;
	
	public ConsistencyCheckerImpl(SparqlEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	/* (non-Javadoc)
	 * @see org.aksw.rex.consistency.ConsistencyChecker#getConsistentTriples(java.util.Set, java.util.Set)
	 */
	@Override
	public Set<Triple> getConsistentTriples(Set<Triple> triples, Set<Axiom> axioms) {
		
		Individual subject;
		Individual object;
		for (Triple triple : triples) {
			subject = new Individual(triple.getSubject().getURI());
			object = new Individual(triple.getObject().getURI());
			
			//get the types of the subject
			Set<NamedClass> subjectTypes = reasoner.getTypes(subject);
			//get the types of the object
			Set<NamedClass> objectTypes = reasoner.getTypes(object);
			
			
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.aksw.rex.consistency.ConsistencyChecker#generateAxioms(org.dllearner.kb.sparql.SparqlEndpoint)
	 */
	@Override
	public Set<Axiom> generateAxioms(SparqlEndpoint endpoint) {
		Set<Axiom> disjointnessAxioms = new HashSet<Axiom>();
		
		SparqlEndpointKS ks = new SparqlEndpointKS(endpoint);
		
		SPARQLReasoner reasoner = new SPARQLReasoner(ks, "cache/sparql");
		reasoner.precomputeClassPopularity();
		
		DisjointClassesLearner learner = new DisjointClassesLearner(ks);
		learner.setReasoner(reasoner);
		
		//get all OWL classes in KB
		Set<NamedClass> classes = reasoner.getOWLClasses();
		
		//compute the disjoint classes for each class
		for (NamedClass cls : classes) {
			learner.setClassToDescribe(cls);
			learner.start();
			List<Axiom> currentlyBestAxioms = learner.getCurrentlyBestAxioms(accuracyThreshold);
			disjointnessAxioms.addAll(currentlyBestAxioms);
		}
		
		return disjointnessAxioms;
	}
	
}
