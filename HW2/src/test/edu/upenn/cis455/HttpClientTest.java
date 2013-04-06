package test.edu.upenn.cis455;

import edu.upenn.cis455.httpclient.HttpClient;
import junit.framework.TestCase;

public class HttpClientTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testHttpClient() {
		String html = "http://examples.oreilly.com/9780596002527/";
		HttpClient client = new HttpClient(html);
		System.out.println(client.getHeadRequestResponse());
		System.out.println(client.getDocument());
	}

}
