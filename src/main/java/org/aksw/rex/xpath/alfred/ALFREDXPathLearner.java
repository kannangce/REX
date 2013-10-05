package org.aksw.rex.xpath.alfred;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import model.ExtractedValue;
import model.MaterializedPageSet;
import model.Page;
import model.Rule;
import model.RuleSet;

import org.aksw.rex.crawler.CrawlIndex;
import org.aksw.rex.results.ExtractionResult;
import org.aksw.rex.results.ExtractionResultImpl;
import org.aksw.rex.util.Pair;
import org.aksw.rex.xpath.XPathLearner;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.util.SimpleIRIShortFormProvider;
import org.slf4j.LoggerFactory;

import rules.xpath.XPathRule;
import alfcore.AlfCoreFacade;
import alfcore.AlfCoreFactory;

import com.hp.hpl.jena.rdf.model.Resource;

public class ALFREDXPathLearner implements XPathLearner {

	private org.slf4j.Logger log = LoggerFactory.getLogger(ALFREDXPathLearner.class);
	private CrawlIndex index;
	IRIShortFormProvider sfp = new SimpleIRIShortFormProvider();
	private ALFREDSampler samplerLeft;
	private ALFREDSampler samplerRight;
	private int numberTrainingPages;
	private List<Page> trainingPages;

	public ALFREDXPathLearner(CrawlIndex index, int trainingPages) {
		this.index = index;
		this.numberTrainingPages = trainingPages;
		this.trainingPages = null;
	}

	public ALFREDXPathLearner(CrawlIndex crawlIndex) {
		this(crawlIndex, Integer.MAX_VALUE);
	}

	@Override
	public List<Pair<XPathRule, XPathRule>> getXPathExpressions(Set<Pair<Resource, Resource>> posExamples, Set<Pair<Resource, Resource>> negExamples, URL Domain) {
		List<Pair<XPathRule, XPathRule>> res = new LinkedList<Pair<XPathRule, XPathRule>>();

		Map<String, List<String>> page2valueLeft = new HashMap<String, List<String>>();
		Map<String, List<String>> page2valueRight = new HashMap<String, List<String>>();

		// TODO to optimize and not retrieving used pages
		ALFREDPageRetrieval pageRetr = new ALFREDPageRetrieval(this.index);
		List<Page> pages = pageRetr.getPages(posExamples, page2valueLeft, page2valueRight, Domain);
		pages = pages.size() > this.numberTrainingPages ? pages.subList(0, this.numberTrainingPages) : pages;
		log.debug("N found pages: " + pages.size());

		if (!pages.isEmpty()) {
			Page firstPage = pages.get(0);
			log.debug("First page: " + firstPage.getTitle());

			this.samplerLeft = null;
			XPathRule left = (XPathRule) this.learnXPath(page2valueLeft, pages, firstPage);
			XPathRule right = (XPathRule) this.learnXPath(page2valueRight, pages, firstPage);

			res.add(new Pair<XPathRule, XPathRule>(left, right));
		} else {
			log.debug("Error: No page found");
		}

		this.trainingPages = pages;
		return res;
	}

	private Rule learnXPath(Map<String, List<String>> page2value, List<Page> pages, Page firstPage) {
		AlfCoreFacade facade = AlfCoreFactory.getSystemFromConfiguration(false, 10, 10, 1, 1, 10000, "Entropy", 0.6);
		facade.setUp("DBPedia", new MaterializedPageSet(pages));

		RuleSet rules = facade.firstSamples(page2value);

		Rule res = rules.getAllRules().get(0);

		if (this.samplerLeft == null) {
			this.samplerLeft = generateSampler(rules);
		} else {
			this.samplerRight = generateSampler(rules);
		}

		// TODO apply sampler on all sub-domain pages
		// sampler.find( ??? );
		// this.log.info("N represented pages: "+sampler.getRepresentedPages().size());
		// this.log.info("N non represented pages: "+sampler.getNonRepresentedPages().size()+" (representative: "+
		// sampler.getRepresentativePages().size()+")");
		return res;
	}

	/**
	 * @return null if getXPathExpressions was never called left rule sampler
	 *         otherwise
	 */
	public ALFREDSampler getSamplerLeft() {
		return this.samplerLeft;
	}

	/**
	 * @return null if getXPathExpressions was never called right rule sampler
	 *         otherwise
	 */
	public ALFREDSampler getSamplerRight() {
		return this.samplerRight;
	}

	private ALFREDSampler generateSampler(RuleSet rules) {
		
		List<List<Rule>> rulesList = new LinkedList<List<Rule>>();
		// build rulesSets - TODO is it always together?
		rulesList.add(rules.getAllRules());

		ALFREDSampler sampler = new ALFREDSampler(rules.getAllRules());
		return sampler;
	}

	@Override
	public Set<ExtractionResult> getExtractionResults(List<Pair<XPathRule, XPathRule>> expressions, URL domain) {
		Set<ExtractionResult> ex = new HashSet<ExtractionResult>();
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			try {
				ArrayList<Pair<String, String>> doc = index.getDocument(i);
				Page d = new Page(doc.get(0).getRight(), null, doc.get(0).getLeft());
				if (d.getTitle().startsWith(domain.toExternalForm())) {
					for (Pair<XPathRule, XPathRule> p : expressions) {
						XPathRule left = p.getLeft();
						XPathRule right = p.getRight();
						ExtractedValue s = left.applyOn(d);
						ExtractedValue o = right.applyOn(d);
						ex.add(new ExtractionResultImpl(s.getTextContent(), o.getTextContent(),d.getTitle()));
					}
				}
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}
		}
		return ex;
	}

	public CrawlIndex getIndex() {
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.aksw.rex.xpath.XPathLearner#setUseExactMatch(boolean)
	 */
	@Override
	public void setUseExactMatch(boolean useExactMatch) {

	}

	public List<Page> getTrainingPages() {
		return this.trainingPages;
	}
} 