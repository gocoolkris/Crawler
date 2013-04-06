package edu.upenn.cis455.dbservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import com.sleepycat.je.Environment;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

import edu.upenn.cis455.dbobjects.ChannelContent;

/**
 * This class operates on ChannelContent and provides
 * API method for accessing the database.
 * @author gokul
 *
 */
public class ChannelContentService {

	public ChannelContentService(){}
	
	private EntityStore store;
	private Environment env;
	private PrimaryIndex<String, ChannelContent> chid_url;
	public ChannelContentService(EntityStore store, Environment e){
		this.store = store;
		this.env = e;
		chid_url = store.getPrimaryIndex(String.class, ChannelContent.class);
	}
	
	/**
	 * Adds the specified documenturl to the given channelid.
	 * @param channelid - the channelid 
	 * @param url - the url that matches the channel's xpath condition.
	 */
	public void addDocument(String channelid, String url){
		try{
			ChannelContent c = chid_url.get(channelid);
			if(c == null){
				c = new ChannelContent();
				c.setChannelid(channelid);
				c.addUrlToList(url);
			}
			else{
				c.addUrlToList(url);
			}
			chid_url.put(c);
			env.sync();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * removes the current Document Url from the channel's
	 * matching documents.
	 * @param channelid - channelid 
	 * @param url - the url that is to be removed.
	 */
	public void removeDocument(String channelid, String url){
		EntityCursor<ChannelContent> list = chid_url.entities();
		try{
			for(ChannelContent c : list){
				if(c.getChannelid().equals(channelid)){
					HashSet<String> urls = c.getUrlList();
					urls.remove(url);
					chid_url.put(c);
					env.sync();
					return;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			list.close();
		}
	}
	
	
	/**
	 * 
	 * @param channelid - the id for which the urls are to be collected.
	 * @return a hashset of all the urls for  a particular Channel.
	 */
	public HashSet<String> getChannelUrls(String channelid){
		
		EntityCursor<ChannelContent> channelsurl = chid_url.entities();
		try{
			System.out.println(channelid + "getting into the getChannelUrls");
			for(ChannelContent c : channelsurl){
				System.out.println(c.getChannelid());
				if(c.getChannelid().equals(channelid)){
					return c.getUrlList();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			channelsurl.close();
		}
		return null;
	}
	
	/**
	 * retrives all Document urls of all channels existing in the database
	 * @return essentially all the channels with its matching urls.
	 */
	public HashMap<String,HashSet<String>> allDocumentUrl(){
		HashMap<String,HashSet<String>> cid_urlList = 
				new HashMap<String,HashSet<String>>();
		EntityCursor<ChannelContent> contents = chid_url.entities();
		try{	
			for(ChannelContent c : contents){
				cid_urlList.put(c.getChannelid(), c.getUrlList());
			}
			return cid_urlList;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			contents.close();
		}
	}

	/**
	 * helper method to print the values for validation.
	 * this helps in validating the correct functioning
	 * of the API methods.
	 */
	public void printDocuments(){
		EntityCursor<ChannelContent> cidurl = chid_url.entities();
		try{
			for(ChannelContent c : cidurl){
				StringBuffer s = new StringBuffer();
				for(String url : c.getUrlList()){
					s.append(url + "\n");
				}
				System.out.println(c.getChannelid()+":" + s.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cidurl.close();
		}
	}
	
	
}
