package test.edu.upenn.cis455;

import edu.upenn.cis455.crawler.XPathCrawler;
import junit.framework.TestCase;

public class XPathCrawlerTest extends TestCase {

	XPathCrawler crawler;
	
	public void testCrawler(){
		String seedUrl = "http://crawltest.cis.upenn.edu/";
		String dbDirectory = "/home/cis455/db";
		String filesizeLimit = "10"; //in MB
		String downloadlimit =  "10000";
		String args[] = new String[]{seedUrl, dbDirectory,filesizeLimit,downloadlimit};
		XPathCrawler.main(args);
	}

}
