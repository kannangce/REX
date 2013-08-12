package org.aksw.rex.domainidentifier;

import java.net.URL;
import java.util.Set;


import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.aksw.rex.util.Pair;

public interface DomainIdentifier {
    URL getDomain(Property p, Set<Pair<Resource, Resource>> posExamples, Set<Pair<Resource, Resource>> negExamples, boolean useCache);
}