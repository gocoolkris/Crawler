package test.edu.upenn.cis455;

import java.util.HashSet;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.dbservice.ChannelContentService;
import edu.upenn.cis455.dbservice.DbStoreSetup;
import junit.framework.TestCase;

public class ChannelContentServiceTest extends TestCase {

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

	public void testAddDocument() {
		ChannelContentService ccs = new ChannelContentService(store, setup.getEnvironment());
		ccs.addDocument("76b40472-3f70-4bbd-84e6-c2551702c31e", "http://yahoo.com");
		HashSet<String> ch = ccs.getChannelUrls("76b40472-3f70-4bbd-84e6-c2551702c31e");
		assertNotNull(ch);
		assertTrue(ch.contains("http://yahoo.com"));
	}

	public void testRemoveDocument() {
		ChannelContentService ccs = new ChannelContentService(store, setup.getEnvironment());
		ccs.addDocument("76b40472-3f70-4bbd-84e6-c2551702c31e", "http://yahoo.com");
		ccs.removeDocument("76b40472-3f70-4bbd-84e6-c2551702c31e", "http://google.com");
		assertNotNull(ccs.getChannelUrls("76b40472-3f70-4bbd-84e6-c2551702c31e"));
		ccs.removeDocument("76b40472-3f70-4bbd-84e6-c2551702c31e", "http://yahoo.com");
		assertTrue(ccs.getChannelUrls("76b40472-3f70-4bbd-84e6-c2551702c31e").isEmpty());
		ccs.printDocuments();
	}

}
