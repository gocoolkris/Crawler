package edu.upenn.cis455.components;

import java.util.Date;
import java.util.HashMap;

import edu.upenn.cis455.dbservice.ServiceProvider;

public class GlobalData {
    //copy of the urls that was previously crawled and stored in database.
	public static HashMap<String,Date> dbCopyOfUrls;
	//list of channelids and their corresponding xpaths.
	public static HashMap<String,String> channelidXpaths;
	//the static instance that provides services and operates on database.
	public static ServiceProvider serviceProvider;
	//a table containing the list of domains that has been
	//previously crawled along with their delay time.
	public static CrawlDelayTable delayTable;
	//a static instance that is used for counting the number of files downloaded
	public static FilesDownloadedCounter counter;
	//maximum number of files that is to be crawled.
	public static int fileCrawlingLimit = Integer.MAX_VALUE;
	//maximum size of files that can be downloaded.
	public static int fileSizeLimitInMb = 100;
	//shutdown the crawling process.
	public static boolean shutdown = false;
	//number of threads that is to be used for crawling.
	public static final int CRAWLER_THREADCOUNT = 10;
	//the static queue for storing the urls that is to be crawled.
	public static Queue urlQueue;
	
}
