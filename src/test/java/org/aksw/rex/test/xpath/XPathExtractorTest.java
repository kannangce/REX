package org.aksw.rex.test.xpath;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aksw.rex.util.Pair;
import org.aksw.rex.xpath.XPathExtractor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import rules.xpath.XPathRule;

public class XPathExtractorTest {
	private org.slf4j.Logger log = LoggerFactory.getLogger(XPathExtractorTest.class);
	private static XPathExtractor ex;

	@BeforeClass
	public static void init(){
		ex = new XPathExtractor();
	}
	@AfterClass
	public static void finish(){
		ex.getIndex().close();
	}
	@Test
	public void testXPathExtractor() throws Exception {
		
		// @formatter:off
		String html = "<html><body>" + 
		"<p>Tom is great</p>" + 
		"<div>" + 
		"     <b>Tom isn't that bad</b>" + 
		"     <p>Tommy boy is not nice instead.</p>"+ 
		"     <div>"+
		"         <p> Another Tom hides here</p>  " +
		"     </div>"+
		"     <div>"+
		"         <p>Tom</p>  " +
		"     </div>"+
		"</div>" +
		"<p>Kate does not like Tom anymore</p>" + 
		"</body></html>";
		// @formatter:on
		String query = "Tom";
		List<String> paths = ex.extractXPaths(query, html);
		for (String path : paths) {
			log.debug(path);
		}
		assertTrue("Paths may not zero in this example", paths.size() > 0);
		assertTrue("One is the correct number of Xpaths", paths.size() == 1);
	}

	@Test
	public void testXPathExtractorOnTable() throws Exception {
		// @formatter:off
		String html = "<html><body>" 
		+ "<table border=>"
		+ "  <tr>"
		+ "    <th>Berlin</th>"
		+ "    <th>Hamburg</th>"
		+ "    <th>M&uuml;nchen</th>"
		+ "  </tr>"
		+ "  <tr>"
		+ "    <td>Berlin</td>"
		+ "    <td>Kiez</td>"
		+ "    <td>Bierdampf</td>"
		+ "  </tr>"
		+ "  <tr>"
		+ "    <td>Berlin</td>"
		+ "    <td>Frikadellen</td>"
		+ "    <td>Fleischpflanzerl</td>"
		+ "    </tr>"
		+ "</table>" + 
		"</body></html>";
		// @formatter:on
		String query = "Berlin";
		List<String> paths = ex.extractXPaths(query, html);
		for (String path : paths) {
			log.debug(path);
		}
		assertTrue("Paths may not zero in this example", paths.size() > 0);
		assertTrue("Three is the correct number of Xpaths", paths.size() == 3);
	}
	
	@Test
	public void testXPathsForTomCruise() throws Exception {
		Map<String, List<Pair<XPathRule, XPathRule>>> data = ex.extractPathsFromCrawlIndexWithURL("Tom Cruise", "Mission Impossible");
		log.debug("#URL with Tom Cruise: " + data.size());
		
		for (Entry<String, List<Pair<XPathRule, XPathRule>>> entry : data.entrySet()) {
			String key = entry.getKey();
			Collection<Pair<XPathRule, XPathRule>> paths = entry.getValue();
			log.debug(key);
			for (Pair<XPathRule, XPathRule> path : paths) {
				log.debug(path.toString());
			}
		}
		assertTrue("Paths may not zero in this example", data.size() > 0);
	}
}
