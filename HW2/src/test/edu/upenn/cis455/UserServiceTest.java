package test.edu.upenn.cis455;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.dbobjects.User;
import edu.upenn.cis455.dbservice.DbStoreSetup;
import edu.upenn.cis455.dbservice.UserService;
import junit.framework.TestCase;

public class UserServiceTest extends TestCase {

	EntityStore store;
	DbStoreSetup setup;
	protected void setUp() throws Exception {
	    setup = new DbStoreSetup("/home/cis455/db",false);
		store = setup.getEntityStore();	
	}
	
	protected void tearDown() throws Exception{
		setup.getEnvironment().sync();
		setup.close();
	}
	
	
	public void testPersistence(){
		UserService usrService = new UserService(store,setup.getEnvironment());
		assertTrue(usrService.addUser("gokul", "gokul"));
		assertFalse(usrService.addUser("gokul", "krishnan"));
		usrService.printUser("gokul");
		usrService.printUser("krishnan");
	}
	
	
	public void testDeletion(){
		UserService usrService = new UserService(store,setup.getEnvironment());
		usrService.addUser("a", "a");
		usrService.deleteUser("a");
		assertFalse(usrService.userNameAlreadyExists("a"));
	}
	
	public void testFunctions(){
		UserService usrService = new UserService(store,setup.getEnvironment());
		usrService.addUser("b","b");
		assertFalse(usrService.validateLogin("b", "a"));
		assertTrue(usrService.validateLogin("b", "b"));
	}
	
}
