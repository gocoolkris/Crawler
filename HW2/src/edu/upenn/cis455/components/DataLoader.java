package edu.upenn.cis455.components;

import java.util.Date;
import java.util.HashMap;

import edu.upenn.cis455.dbservice.ChannelService;
import edu.upenn.cis455.dbservice.DocumentService;

/**
 * Loads the list of urls that was previously saved into the Database
 * Loads the list of channelid and their corresponding Xpaths from database.
 * @author gokul
 *
 */
public class DataLoader {


	/**
	 * Loads the list of urls that was previously crawled and saved to database.
	 * @param ds - document service.
	 */
	public static void LoadUrlsFromDatabase(DocumentService ds){
		GlobalData.dbCopyOfUrls = new HashMap<String,Date>();
		GlobalData.dbCopyOfUrls = ds.getUrlList();
	}
	
	/**
	 * loads the list of channelid along with their xpaths.
	 * @param cs - ChannelService
	 */
	public static void LoadChannelidXpathsFromDatabase(ChannelService cs){
		GlobalData.channelidXpaths = new HashMap<String,String>();
		GlobalData.channelidXpaths = cs.getChannelXpaths();
	}
	
}
