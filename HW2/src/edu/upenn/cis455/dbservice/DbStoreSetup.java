package edu.upenn.cis455.dbservice;

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

/**
 * this class is solely responsible for initializing
 * and setting up the database that is to be used for
 * crawling and the UI for interaction.
 * @author gokul
 *
 */
public class DbStoreSetup {

	private  String envLocation, storeName = "CrawlerDataStore";
	private Environment environment;
	private EntityStore store;
	private boolean readOnly;
	
	public DbStoreSetup(){}
	
	/**
	 * Takes the location of an existing Database/one to be created.
	 * @param location - location of the Database.
	 * @param rOnly - read operation mode
	 */
	public DbStoreSetup(String location, boolean rOnly){
		envLocation = location;
		readOnly = rOnly;
		setUp();
	}
	
	/**
	 * sets up the the initial configuration that is
	 * to be used for Database environment.
	 */
	private void setUp(){
		try{
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setLockTimeout(1000, TimeUnit.MILLISECONDS);
		StoreConfig strConfig = new StoreConfig();
		envConfig.setReadOnly(readOnly);
        strConfig.setReadOnly(readOnly);
        //if needed to write
        envConfig.setAllowCreate(!readOnly);
        strConfig.setAllowCreate(!readOnly);
		
        File path = new File(envLocation);
        if(!path.exists()) path.mkdirs();
        
        environment = new Environment(path,envConfig);
        store = new EntityStore(environment,storeName,strConfig);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * getter for EntityStore Object.
	 * @return the EntityStore object
	 */
	public EntityStore getEntityStore(){
		return store;
	}
	
	/**
	 * getter for the environment object.
	 * @return the environment object that contains the database configurations
	 */
	public Environment getEnvironment(){
		return environment;
	}
	
	/**
	 * Shutsdown the database environment and the database safely.
	 */
	public void close(){
		try{
			if(store != null) store.close();
			if(environment != null) environment.close();
		}
		catch(Exception e){}
	}
	
}
