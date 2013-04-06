package edu.upenn.cis455.dbservice;

import com.sleepycat.je.Environment;
import com.sleepycat.persist.EntityStore;

/**
 * The class is used for the creating and maintaining all services
 * in one place. It setups the database. it instantiates different services
 * and shuts them down at the end of the crawling.
 * @author gokul
 */
public class ServiceProvider {

	public ServiceProvider(){}

	private EntityStore store;
	private Environment env;
	private ChannelContentService ccs;
	private ChannelService channelService;
	private UserService userService;
	private DocumentService documentService;
	private SubscriberService subscriberService;


	/**
	 * Method that setsup the database environment.
	 * @param location - the location of the database to be used/setup
	 * @param openInReadOnly - is to be opened in readOnly mode
	 */
	public void setUpDb(String location, boolean openInReadOnly){
		DbStoreSetup setup = new DbStoreSetup(location, openInReadOnly);
		store = setup.getEntityStore();
		env = setup.getEnvironment();
	}

	/**
	 * instantiates all the dbservices that would interact
	 * with the database. The crawler in essence would have
	 * access to just one instance of all the objects.
	 */
	public void instantiateServices(){
		ccs = new ChannelContentService(store, env);
		channelService = new ChannelService(store, env);
		userService = new UserService(store, env);
		documentService = new DocumentService(store, env);
		subscriberService = new SubscriberService(store, env);
	}

	/**
	 * getter for ChannelContentService object.
	 * @return instance of ChannelContentService
	 */
	public ChannelContentService getCcs() {
		return ccs;
	}

	/**
	 * getter for ChannelService.
	 * @return an instance of ChannelService.
	 */
	public ChannelService getChannelService() {
		return channelService;
	}

	/**
	 * getter for UserService.
	 * @return returns an instance of UserService.
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * getter for DocumentService.
	 * @return an instance of DocumentService.
	 */
	public DocumentService getDocumentService() {
		return documentService;
	}

	/**
	 * getter for SubscriberService.
	 * @return an instance of SubscriberService.
	 */
	public SubscriberService getSubscriberService() {
		return subscriberService;
	}
	
	/**
	 * helper method that shutdowns all the services
	 * and database safely.
	 */
	public void shutdownServices(){
		ccs = null;
		channelService = null;
		userService = null;
		documentService = null;
		subscriberService = null;
	}
	
	/**
	 * shutsdown the database.
	 */
	public void shutdownDatabase(){
		try{
		env.sync();
		store.close();
		env.close();
		}catch(Exception e){}
	}
	
	
}
