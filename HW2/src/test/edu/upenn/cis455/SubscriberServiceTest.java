package test.edu.upenn.cis455;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.dbservice.DbStoreSetup;
import edu.upenn.cis455.dbservice.SubscriberService;
import junit.framework.TestCase;

public class SubscriberServiceTest extends TestCase {

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

	public void testSubscribe() {
		SubscriberService ss = new SubscriberService(store, setup.getEnvironment());
		ss.subscribe("gokul", "CMP", "krishnan");
		assertTrue(ss.getSubscriptionList("krishnan") != null);
	}

	public void testUnsubscribe() {
		SubscriberService ss = new SubscriberService(store, setup.getEnvironment());
		ss.unsubscribe("gokul", "CMP", "krishnan");
		ss.printSubscriptionList();
		assertTrue(ss.getSubscriptionList("krishnan").isEmpty());
	}

}
