package org.aksw.rex.results;

/**
 * Interface to keep subject, object pairs, given a prominence url. <br/><br/>
 * <b>TODO: No interface needed to beans. To be removed once the tool is
 * stabilized.</b>
 * 
 * @author r.usbeck
 *
 *
 */
public interface ExtractionResult {
	/**
	 * 
	 * @return subject
	 */
	public String getSubject();

	/**
	 * 
	 * @return object
	 */
	public String getObject();

	/**
	 * 
	 * @param subject
	 */
	public void setSubject(String s);

	/**
	 * 
	 * @param object
	 */
	public void setObject(String o);

	/**
	 * 
	 * @return prominence url
	 */
	String getPageURL();

	/**
	 * 
	 * @param prominence url
	 */
	void setPageURL(String pageURL);
}
