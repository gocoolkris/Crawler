package edu.upenn.cis455.dbobjects;
import java.util.HashSet;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.SecondaryKey;
import com.sleepycat.persist.model.DeleteAction;

import static com.sleepycat.persist.model.Relationship.*;

/**
 * This class represents a subscriber to a particular channel.
 * Only a registered user could be subscriber.
 * @author gokul
 *
 */
@Entity
public class Subscriber {

	public Subscriber(){}

	@PrimaryKey
	String subscriber;

	HashSet<String> channelid;

	/**
	 * returns the subscriber name
	 * @return
	 */
	public String getSubscriber() {
		return subscriber;
	}

	/**
	 * setter for subsriber.
	 * @param subscriber
	 */
	public void setSubscriber(String subscriber) {
		this.subscriber = subscriber;
	}

	/**
	 * gets the list of channelid of the current
	 * subsriber.
	 * @return a hashset of channelids
	 */
	public HashSet<String> getChannelids() {
		return channelid;
	}

	/**
	 * removes a particular channelid from
	 * the subscribed list.
	 * @param id - the channelid
	 */
	public void removeChannelid(String id){
		channelid.remove(id);
	}
	
	
	/**
	 * adds a particular channelid to the 
	 * list of subscription.
	 * @param cid - the channel id.
	 */
	public void addChannelId(String cid){
		if(channelid == null)
			channelid = new HashSet<String>();
		channelid.add(cid);
	}
}
