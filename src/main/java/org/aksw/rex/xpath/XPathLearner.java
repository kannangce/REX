package org.aksw.rex.xpath;

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.aksw.rex.crawler.CrawlIndex;
import org.aksw.rex.results.ExtractionResult;
import org.aksw.rex.util.Pair;

import rules.xpath.XPathRule;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Interface for XPathLearner modules. Responsible for learning the xpath rules
 * based on the positive examples provided.
 * 
 * The implementation is expected to use the webpages stored in the
 * {@link CrawlIndex} to learn the xpaths.
 * 
 * @author r.usbeck
 *
 */
public interface XPathLearner {

	/**
	 * Gets the number of training points and returns a list of possible pairs of
	 * extraction rules
	 * 
	 * @param posExamples Positive examples to be used.
	 * @param Domain      The domain whose pages from the {@link CrawlIndex} will be
	 *                    used to learn.
	 * @return Collection of {@link Pair} of {@link XPathRule} where the left
	 *         contains the {@link XPathRule} for subject and right contains the
	 *         {@link XPathRule} for the object.
	 */
	List<Pair<XPathRule, XPathRule>> getXPathExpressions(Set<Pair<Resource, Resource>> posExamples, URL Domain);

	/**
	 * Returns the collections of {@link ExtractionResult} for the given Collection
	 * of Pair of {@link XPathRule} for the given <b>domain</b> from the
	 * <b>index</b>.
	 * 
	 * @param expressions Collection of {@link Pair} of {@link XPathRule} using
	 *                    which the extraction to be done.
	 * @param domain      The domain for which the extraction to be done.
	 * @return Collection of {@link ExtractionResult} containing the &lt;S,O&gt; for
	 *         the given {@link XPathRule} from the matched pages from the index.
	 */
	Set<ExtractionResult> getExtractionResults(List<Pair<XPathRule, XPathRule>> expressions, URL domain);

	/**
	 * Whether to use exact matching or containment for node matching task in DOM
	 * tree.
	 * 
	 * @param useExactMatch true if exact match is needed, false otherwise.
	 */
	void setUseExactMatch(boolean useExactMatch);

}
