package edu.upenn.cis455.dbservice;

import java.util.HashSet;

import com.sleepycat.je.Environment;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

import edu.upenn.cis455.dbobjects.Channel;
import edu.upenn.cis455.dbobjects.Subscriber;

/**
 * This class operates on Subscriber object.
 * @author gokul
 *
 */
public class SubscriberService {


	public SubscriberService(){}

	private EntityStore store;
	private PrimaryIndex<String, Subscriber> user_channelid;
	private Environment env;
	public SubscriberService(EntityStore str, Environment e){
		store = str;
		env = e;
		user_channelid = store.getPrimaryIndex(String.class, Subscriber.class);
	}

	/**
	 * this method takes in parameters and subscribes to a specific channel
	 * owned by another user.When the user who owns the channel is deleted, 
	 * the subscription also ends.
	 * @param owner - the owner of the channel.
	 * @param channelname - the name of the channel.
	 * @param subscriber - the user who wants to subscribe to the channel.
	 * @return true if subscribed, false otherwise.
	 */
	public boolean subscribe(String owner, String channelname, String subscriber){
		try{
			if(owner.equals(subscriber)) return false;
			ChannelService cs = new ChannelService(store,env);
			Channel c = cs.getChannel(channelname, owner);
			String cid = c.getChannelid();
			Subscriber us = user_channelid.get(subscriber);
			if(us == null){
				System.out.println("creating and adding new entries");
				us = new Subscriber();
				us.setSubscriber(subscriber);
				us.addChannelId(cid);
			}
			else{
				System.out.println("adding new entries");
				us.addChannelId(cid);
			}
			user_channelid.put(us);
			env.sync();
			System.out.println("returning true");
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * removes the Channelid from all the subscribers. This method is 
	 * called when a particular user is deleted. All his  channel are
	 * unsubscribed from other users.
	 * @param channelid
	 */
	public void removeChannelidFromAllSubscribers(String channelid){
		EntityCursor<Subscriber> subscribers = user_channelid.entities();
		try{
			for(Subscriber s: subscribers){
					s.removeChannelid(channelid);
					user_channelid.put(s);
					env.sync();
					return;
			}
		}catch(Exception e){}
		finally{
			subscribers.close();
		}
	}
	
	
	/**
	 * this method is called if a registered user but not a subscriber of
	 * the channel wants to unsubscribe to the service.
	 * @param owner - the owner of the channel.
	 * @param channelname - the name of the channel.
	 * @param subscriber - the subscriber of the channel.
	 */
	public void unsubscribe(String owner, String channelname, String subscriber){
		
		try{
			ChannelService cs = new ChannelService(store,env);
			Channel c = cs.getChannel(channelname, owner);
			String cid = c.getChannelid();
			System.out.println("Obtained cid" + cid);
			Subscriber us = user_channelid.get(subscriber);
			if(us != null){
				us.removeChannelid(cid);
				user_channelid.put(us);
				env.sync();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{}
	}

	/**
	 * returns the list of all channels to which a particular user
	 * is subscribed to.
	 * @param subscriber  - the name of the subscriber.
	 * @return  a hashset of subscription channels.
	 */
	public HashSet<String> getSubscriptionList(String subscriber){
		try{
			Subscriber s = user_channelid.get(subscriber);
			if(s != null) return s.getChannelids();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * prints the list of subscriber name along with the list of channels 
	 * they are subscribed to.
	 */
	public void printSubscriptionList(){
		EntityCursor<Subscriber> records = user_channelid.entities();
		try{
			for(Subscriber s : records){
				StringBuffer sb = new StringBuffer();
				for(String cid : s.getChannelids()){
					sb.append(cid + "\n");
				}
				System.out.println(s.getSubscriber() + ":" + sb);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			records.close();
		}
	}
	
	
}
