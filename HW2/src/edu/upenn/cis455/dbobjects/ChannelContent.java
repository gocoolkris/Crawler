package edu.upenn.cis455.dbobjects;



import java.util.HashSet;


import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;


/**
 * The class represents a ChannelContent. A channel content
 * typically consists of a channelid and list of urls that
 * matches the channel xpaths.
 * @author gokul
 *
 */
@Entity
public class ChannelContent {

	public ChannelContent(){}
	
	
	
	@PrimaryKey
	private String channelid;
	/**
	 * getter for the channel id.
	 * @return channelid
	 */
	public String getChannelid() {
		return channelid;
	}

	/**
	 * setter for the channelid
	 * @param channelid
	 */
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	
	private HashSet<String> urlList;
	/**
	 * getter for the list of url
	 * that needs to be retrieved.
	 * @return
	 */
	public HashSet<String> getUrlList() {
		return urlList;
	}

	/**
	 * the url that needs to be added to the 
	 * list of urls that has been matching for
	 * the channel
	 * @param url
	 */
	public void addUrlToList(String url) {
		if(urlList == null){
			urlList = new HashSet<String>();	
		}
		urlList.add(url);
	}

	
	
}
