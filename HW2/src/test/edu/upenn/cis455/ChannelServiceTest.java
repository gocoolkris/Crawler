package test.edu.upenn.cis455;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.dbobjects.Channel;
import edu.upenn.cis455.dbservice.ChannelService;
import edu.upenn.cis455.dbservice.DbStoreSetup;
import edu.upenn.cis455.dbservice.UserService;
import junit.framework.TestCase;

public class ChannelServiceTest extends TestCase {
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

	public void testGetChannel() {
		ChannelService cs = new ChannelService(store, setup.getEnvironment());
		cs.createChannel("gokul", "Spiritual", "/a\n/html/body", "/a/b.xsl");
		assertNotNull(cs.getChannel("Spiritual", "gokul"));
		cs.printChannels();
		
	}

	public void testCreateChannel() {
		ChannelService cs = new ChannelService(store, setup.getEnvironment());
		assertTrue(cs.createChannel("gokul", "SS", "content", "xslt"));
		assertNotNull(cs.getChannel("SS", "gokul"));
	}

}
