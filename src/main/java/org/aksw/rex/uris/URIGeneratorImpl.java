package org.aksw.rex.uris;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.aksw.rex.results.ExtractionResult;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Property;

public class URIGeneratorImpl implements URIGenerator {
    private org.slf4j.Logger log = LoggerFactory.getLogger(URIGeneratorImpl.class);
    private SurfaceFormIndex index;

    public URIGeneratorImpl() {
        String file = "en_surface_forms.tsv.gz";
        String idxDirectory = "index/";
        String type = SurfaceFormIndex.TSV;
        String baseURI = "http://dbpedia.org/resource/";
        index = new SurfaceFormIndex(file, idxDirectory, type, baseURI);
    }

    public Set<Triple> getTriples(Set<ExtractionResult> posNegEx, Property p) throws Exception {
        Set<Triple> set = new HashSet<Triple>();
        // for each pos and neg example
        if (posNegEx == null) {
            return set;
        }
        for (ExtractionResult res : posNegEx) {
            // process left
            Triple process = process(res, p);
            if (process != null)
                set.add(process);
        }
        return set;
    }

    // TODO discuss this
    // this does not seem to do what it is supposed to. It should lookup uris for
    // both subject and object, then create a triple out of these uris. The method
    // should always return a uri, even if the resource cannot be found DBpedia
    // The system should take the most prominent URI (or simply use AGDISTIS) to resolve
    // cases where there are several URIs from which we can choose

    private Triple process(ExtractionResult res, Property p) throws Exception {
        String subjectString = res.getSubject();
        String objectString = res.getObject();

        Node s = null;
        Node o = null;
        s = generateURI(subjectString, s);
        o = generateURI(objectString, o);
        Triple t = new Triple(s, p.asNode(), o);
        return t;
    }

    private Node generateURI(String subjectString, Node s) throws URISyntaxException, Exception {
        // lookup in index
        HashSet<String> possibleSubjectURIs = index.search(subjectString);
        // ---DEBUG---//
        log.debug("Number of candidates for Subject: " + possibleSubjectURIs.size());
        for (String tmp : possibleSubjectURIs) {
            log.debug("\t" + subjectString + " -> " + tmp);
        }
        // ---END DEBUG---//
        if (possibleSubjectURIs != null && possibleSubjectURIs.size() == 1) {
            s = NodeFactory.createURI(possibleSubjectURIs.iterator().next());
        } else if (possibleSubjectURIs.size() == 0) {
            // generate URI, if not in index
            URI uri = new URI("http", "aksw.org", "/resource", subjectString);
            log.debug("Constructed URI: " + uri);
            s = NodeFactory.createURI(uri.toString());
        } else {
            throw new Exception("More than one candidate for \"" + subjectString + "\" detected!");
        }
        return s;
    }
}