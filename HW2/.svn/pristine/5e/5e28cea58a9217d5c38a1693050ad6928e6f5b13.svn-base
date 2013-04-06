package test.edu.upenn.cis455;

import java.util.Date;
import java.util.HashMap;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.dbobjects.Document;
import edu.upenn.cis455.dbservice.DbStoreSetup;
import edu.upenn.cis455.dbservice.DocumentService;
import junit.framework.TestCase;

public class DocumentServiceTest extends TestCase {

	EntityStore store;
	DbStoreSetup setup;
	protected void setUp() throws Exception {
		setup = new DbStoreSetup("/home/cis455/db",false);
		store = setup.getEntityStore();
	}

	protected void tearDown() throws Exception {
		setup.getEnvironment().sync();
		setup.close();
	}

	public void testDocument(){
		DocumentService ds = new DocumentService(store, setup.getEnvironment());
		ds.addDocument("http://www.google.com", "Woo Ha");
		String content  = ds.getDocument("http://www.google.com").getDocumentContent();
		assertEquals(content,"Woo Ha");
		ds.printDocument();
	}
	
	public void testDeleteDocument(){
		DocumentService ds = new DocumentService(store, setup.getEnvironment());
		ds.addDocument("http://www.google.com", "Woo Ha");
		ds.deleteDocument("http://www.google.com");
		Document d  = ds.getDocument("http://www.google.com");
		assertNull(d);
	}
	
	public void testGetAllDocuments(){
		DocumentService ds = new DocumentService(store, setup.getEnvironment());
		ds.addDocument("http://www.google.com", "Woo Ha");
		ds.addDocument("a", "b");
		HashMap<String,Date> list = ds.getUrlList();
		assertTrue(list.containsKey("http://www.google.com"));
		assertTrue(list.containsKey("a"));
	}
	
}
