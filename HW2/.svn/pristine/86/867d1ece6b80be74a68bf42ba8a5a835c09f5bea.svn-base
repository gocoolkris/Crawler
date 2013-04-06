package edu.upenn.cis455.dbservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import com.sleepycat.je.Environment;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

import edu.upenn.cis455.dbobjects.Channel;
import edu.upenn.cis455.dbobjects.User;

/**
 * The service object that is to be used for the operations
 * on the Channel Object entity.
 * @author gokul
 *
 */
public class ChannelService {	

	//private HashSet<String> channelids;
	private Environment environ;
	private PrimaryIndex<String,Channel> channelid;
	public ChannelService(EntityStore str, Environment e){
		environ = e;
		//channelids = new HashSet<String>();
		channelid = str.getPrimaryIndex(String.class, Channel.class);
		//loadChannelIds();

	}

	/**
	 * Helper method used for validating the channel details.
	 */
	public void printChannels(){
		EntityCursor<Channel>  channels = channelid.entities();
		for(Channel c : channels){
			System.out.println(c.getChannelid() + ":" + c.getChannelName());
			String[] xp = c.getXpath().split(";");
			for(String s : xp) System.out.println(s);
		}
		channels.close();
	}
	
	/**
	 * method that returns all the channels in the Database.
	 * @return an array list of all the channels that exist in the database.
	 */
	public ArrayList<Channel> getAllChannels(){
		try{
		EntityCursor<Channel> channels = channelid.entities();
		ArrayList<Channel> Channels = new ArrayList<Channel>();
		for(Channel c : channels) Channels.add(c);
		channels.close();
		return Channels;
		}
		catch(Exception e){}
		
		return null;
	}
	
	/**
	 * method that returns the matching channel object for a
	 *  given channelname and the owner. It is to be noted that
	 *  the channelname can be same for different users.
	 * @param channelname - name of the channel
	 * @param owner - the owner of the channel.
	 * @return the channel object.
	 */
	public Channel getChannel(String channelname, String owner){
		EntityCursor<Channel> channels = channelid.entities();;
		try{
			for(Channel c : channels){
				if(c.getChannelName().equals(channelname) &&
						c.getOwner().equals(owner)) return c;
			}
		}catch(Exception e){
		}
		finally{
			channels.close();
		}
		return null;
	}
	
	/**
	 * creates a channel and commits it to the database for a given owner, 
	 * channel name, xpath conditions and xslturl.
	 * @param uname - owner name of the channel.
	 * @param cname - name of the channel.
	 * @param xpath - the xpath of the given channel.
	 * @param xslturl - the xslt url for a given channel
	 * @return true if the channel has been successfully created, false otherwise.
	 */
	public boolean createChannel(String uname, String cname, String xpath, String xslturl){
		try{
			String cid = generateChannelid();
			System.out.println(cid);
			Channel ch = new Channel(cid, cname, uname, xpath, xslturl);
			channelid.put(ch);
			environ.sync();
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	/**
	 * Returns true if a given channel has been deleted.
	 * @param cid - the channel id of the channel to be deleted.
	 * @return true if the channel has been deleted, false
	 * otherwise.
	 */
	public boolean deleteChannel(String cid){
		EntityCursor<Channel> channels = channelid.entities();
		try{
			System.out.println("incoming channelid:" + cid);
			Channel c = channelid.get(cid);
			channelid.delete(c.getChannelid());
			environ.sync();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		} finally{
			channels.close();
		}
	}
	
	/**
	 * Method returns a hashmap of the channel along with 
	 * their xpaths. the crawler method uses this function.
	 * @return a hashmap of the channelid along with their
	 * xpaths.
	 */
	public HashMap<String,String> getChannelXpaths(){
		try{
			HashMap<String,String> xpaths = new HashMap<String,String>();
			EntityCursor<Channel> channels = channelid.entities();
			for(Channel c : channels){
				xpaths.put(c.getChannelid(), c.getXpath());
			}
			return xpaths;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/*
	private void loadChannelIds(){
		try{
			EntityCursor<Channel> channels = channelid.entities();
			for(Channel c : channels){
				channelids.add(c.getChannelid());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}*/

	/**
	 * method used to generate a unique identifier for the channelid.
	 * @return string, which is the unique identifier for the channelid.
	 */
	private String generateChannelid(){
		try{
			UUID uid = UUID.randomUUID();
			return uid.toString();
		}catch(Exception e){
			return null;
		}
	}
}
