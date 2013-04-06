package edu.upenn.cis455.crawler;

import edu.upenn.cis455.components.CrawlDelayTable;
import edu.upenn.cis455.components.DataLoader;
import edu.upenn.cis455.components.FilesDownloadedCounter;
import edu.upenn.cis455.components.GlobalData;
import edu.upenn.cis455.components.Queue;
import edu.upenn.cis455.dbservice.ServiceProvider;


public class XPathCrawler {
	/**
	 * 
	 * @param args[0] - url of the web page to start with
	 * @param args[1] - Database environment directory
	 * @param args[2] - maximum file size to download 
	 * @param args[3] - optional parameter to specify the number of files
	 * to crawl before stopping.
	 */
	private static ThreadPool pool;
	public static void main(String args[])
	{
		if(args.length < 3) return;
		if(args[3] != null){
			GlobalData.fileCrawlingLimit = Integer.parseInt(args[3]);
		}
		initialize(args[0], args[1], args[2]);
		while(true){
			try{
				if(GlobalData.shutdown) break;
				else if(GlobalData.urlQueue.isEmpty() && pool.allThreadsSleeping())
					GlobalData.shutdown = true;
				Thread.sleep(1000);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		System.out.println(GlobalData.counter.getDownloadedFilesCount());
		pool.shutdownThreadPool();
		shutdownServicesAndDb();
	}

	/**
	 * shutsdown the dbobjects(store and environment)
	 * and the Service objects that interacts with the
	 * database.
	 */
	private static void shutdownServicesAndDb() {
		GlobalData.serviceProvider.shutdownServices();
		GlobalData.serviceProvider.shutdownDatabase();	
	}

	/**
	 * initializes the url-queue.
	 */
	private static void initializeUrlQueue() {
		GlobalData.urlQueue = new Queue();
	}

	/**
	 * initializes the database and the services
	 * that operate on them.
	 * @param dbLocation - the location of the database
	 * if one exists/ otherwise a new one is created.
	 */
	private static void initializeDbServices(String dbLocation){
		GlobalData.serviceProvider = new ServiceProvider();
		GlobalData.serviceProvider.setUpDb(dbLocation, false);
		GlobalData.serviceProvider.instantiateServices();

	}

	/**
	 * initializes a set of thread pool that needs to be
	 * instantiated before crawling begins.
	 */
	private static void initializeThreadPool(){
		pool = new ThreadPool();
		pool.startThreadPool();
	}

	/**
	 * loads the database services for an existing
	 * database.If none exists, it creates one.
	 */
	private static void loadDataFromDatabase(){
		DataLoader.LoadUrlsFromDatabase(GlobalData.serviceProvider.getDocumentService());
		DataLoader.LoadChannelidXpathsFromDatabase(GlobalData.serviceProvider.getChannelService());
	}

	/**
	 * the initialize method that initializes different components.
	 * @param sUrl - seeds Url to begin crawling.
	 * @param databaseLocation - the location of the database to operate with.
	 * @param maxFileSize - maximum size of the file that can be downloaded.
	 */
	private static void initialize(String sUrl, String databaseLocation, String maxFileSize){
		try{
			initializeDbServices(databaseLocation);
			loadDataFromDatabase();
			initializeUrlQueue();
			initializeThreadPool();
			GlobalData.counter = new FilesDownloadedCounter();
			GlobalData.urlQueue.enqueueUrl(sUrl);
			GlobalData.delayTable = new CrawlDelayTable();
			if(maxFileSize != null){
				GlobalData.fileSizeLimitInMb = Integer.parseInt(maxFileSize);
			}
		}catch(Exception e){
			System.out.println("Error initializing services");
			e.printStackTrace();
		}
	}

}
