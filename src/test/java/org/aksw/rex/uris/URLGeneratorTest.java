package org.aksw.rex.uris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.aksw.commons.collections.Pair;
import org.aksw.rex.results.ExtractionResult;
import org.aksw.rex.results.ExtractionResultImpl;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.graph.Triple;

public class URLGeneratorTest {
    private org.slf4j.Logger log = LoggerFactory.getLogger(URLGeneratorTest.class);

    @Test
    public void testURLGeneratorEmptySet() throws Exception {
        URIGeneratorImpl gen = new URIGeneratorImpl();
        // filling the test cases
        Set<Pair<ExtractionResult, ExtractionResult>> posNegEx = null;
        Set<Triple> result = gen.getTriples(posNegEx);
        assertEquals("Empty Extraction result should result in empty Triples.", new HashSet<Triple>(), result);
    }

    @Test
    public void testURLGeneratorExistingURLs() throws Exception {
        log.debug("testURLGeneratorExistingURLs");
        URIGeneratorImpl gen = new URIGeneratorImpl();
        // filling the test cases
        ExtractionResultImpl left = new ExtractionResultImpl("Paris");
        ExtractionResultImpl right = new ExtractionResultImpl("London");

        Set<Pair<ExtractionResult, ExtractionResult>> posNegEx = new HashSet<Pair<ExtractionResult, ExtractionResult>>();
        posNegEx.add(new Pair<ExtractionResult, ExtractionResult>(left, null));
        Set<Triple> result = gen.getTriples(posNegEx);
        boolean found = false;
        for (Triple t : result) {
            if (t.getObject().getURI().equals("http://dbpedia.org/resource/Paris"))
                found = true;
        }
        assertTrue("Positiv should be Paris", found);
        assertEquals("One Triple expected.", 1, result.size());

        posNegEx = new HashSet<Pair<ExtractionResult, ExtractionResult>>();
        posNegEx.add(new Pair<ExtractionResult, ExtractionResult>(null, right));
        result = gen.getTriples(posNegEx);
        found = false;
        for (Triple t : result) {
            if (t.getObject().getURI().equals("http://dbpedia.org/resource/London"))
                found = true;
        }
        assertTrue("Negativ should be London", found);
        assertEquals("One Triple expected.", 1, result.size());

        posNegEx = new HashSet<Pair<ExtractionResult, ExtractionResult>>();
        posNegEx.add(new Pair<ExtractionResult, ExtractionResult>(left, right));
        result = gen.getTriples(posNegEx);
        found = false;
        for (Triple t : result) {
            if (t.getObject().getURI().equals("http://dbpedia.org/resource/London"))
                found = true;
        }
        assertTrue("Negativ should be London", found);
        found = false;
        for (Triple t : result) {
            if (t.getObject().getURI().equals("http://dbpedia.org/resource/Paris"))
                found = true;
        }
        assertTrue("Positiv should be Paris", found);
        assertEquals("Two Triple expected.", 2, result.size());

    }

    @Test
    public void testURLGeneratorNonExistingURLs() throws Exception {
        URIGeneratorImpl gen = new URIGeneratorImpl();
        // filling the test cases
        ExtractionResultImpl left = new ExtractionResultImpl("AKSW");

        Set<Pair<ExtractionResult, ExtractionResult>> posNegEx = new HashSet<Pair<ExtractionResult, ExtractionResult>>();
        posNegEx.add(new Pair<ExtractionResult, ExtractionResult>(left, null));
        Set<Triple> result = gen.getTriples(posNegEx);
        boolean found = false;
        for (Triple t : result) {
            if (t.getObject().getURI().equals("http://aksw.org/resource#AKSW"))
                found = true;
        }
        assertTrue("Should be http://aksw.org/resource#AKSW", found);
        assertEquals("One Triple expected.", 1, result.size());

    }
}
