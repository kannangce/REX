/*
 * and open the template in the editor.
 */
package org.aksw.rex.util;

import java.util.List;

/**
 * DAO to store results retrieved while asking Google Search
 * @author ngonga
 */
public class GoogleResults {

    public ResponseData responseData;

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        return "ResponseData[" + responseData + "]";
    }
/**
 * stores the list of Google Results returned
 * @author r.usbeck
 *
 */
    public static class ResponseData {

        private List<Result> results;

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

        @Override
        public String toString() {
            return "Results[" + results + "]";
        }
    }
/**
 * DAO to store single search results (url,title)
 * @author r.usbeck
 *
 */
    public static class Result {

        private String url;
        private String title;

        public String getUrl() {
            return url;
        }
        public String getTitle() {
            return title;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        @Override
		public String toString() {
            return "Result[url:" + url + ",title:" + title + "]";
        }
    }
}
