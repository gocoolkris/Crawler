package test.edu.upenn.cis455;

import org.w3c.dom.Document;

import edu.upenn.cis455.httpclient.HttpClient;
import edu.upenn.cis455.validator.DoMTreeBuilder;
import edu.upenn.cis455.xpathengine.XPathEngineImpl;
import junit.framework.TestCase;

public class XPathEngineImplTest extends TestCase {

	XPathEngineImpl impl;
	protected void setUp() throws Exception {
		super.setUp();

	}

	public void testIsValid() {
		String[] xpaths = new String[]{
				"/a/foo/bar/abc[[]]]",
				"/aa//b",
				"/a/b[c[@d = \"e\"]]",
				"/a/b/c[@d = \"e\"][text()=\"f\"][contains(text(),\"g\")]"
		};
		impl = new XPathEngineImpl();
		impl.setXPaths(xpaths);
		assertFalse(impl.isValid(-10));
		assertFalse(impl.isValid(0));
		assertFalse(impl.isValid(1));
		assertTrue(impl.isValid(2));
		assertTrue(impl.isValid(3));
		assertFalse(impl.isValid(10));
	}

	public void testEvaluateSet1() {
		String url = "http://crawltest.cis.upenn.edu/nytimes/";
		HttpClient c = new HttpClient(url);
		String contents = c.getDocument().toString();
		DoMTreeBuilder builder = new DoMTreeBuilder(contents,url);
		Document d = builder.getDocumentNode();
		String[] xpaths = new String[]{
				"/a/foo/bar/abc",
				"/aa/b",
				"/a/b[c[@d = \"e\"]]",
				"/a/b/c[@d = \"e\"][text()=\"f\"][contains(text(),\"g\")]"
		};
		impl = new XPathEngineImpl();
		impl.setXPaths(xpaths);
		boolean[] arr = impl.evaluate(d);
		for(int i = 0; i < arr.length; ++i)
			assertFalse(arr[i]);
	}

	public void testEvaluateSet2() {
		String url = "http://crawltest.cis.upenn.edu/nytimes/";
		HttpClient c = new HttpClient(url);
		String contents = c.getDocument().toString();
		DoMTreeBuilder builder = new DoMTreeBuilder(contents,url);
		Document d = builder.getDocumentNode();
		String[] xpaths = new String[]{
				"/html/head/title",
				"/html/head/title[contains(text(), \"NY\")]",
				"/html/body/h2[@align = \"center\"]",
				"/html/body/ul/li/a[@href = \"Africa.xml\"]",
				"/html/body/ul/li/a[@href = \"Africa.xml\"][contains(text(),\"America\")]"
		};
		impl = new XPathEngineImpl();
		impl.setXPaths(xpaths);
		boolean[] arr = impl.evaluate(d);
		for(int i = 0; i < arr.length - 1; ++i)
			assertTrue(arr[i]);
		assertFalse(arr[arr.length  - 1]);
	}
	
	
	
}
