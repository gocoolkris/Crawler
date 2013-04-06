package edu.upenn.cis455.dbobjects;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.SecondaryKey;
import com.sleepycat.persist.model.Relationship;

/**
 * @author gokul
 * This class stores the channel information.
 * The primary key is the channelid that is randomly
 * generated. It also stores other channel information
 * like xpath, xslturl, owner name channel name.
 */
@Entity
public class Channel {

	public Channel(){}

	public Channel(String cid, String cname, String uname,String xpath, String xslt){
		channelid = new String(cid);
		channelName = cname;
		owner = uname;
		this.xpath = xpath;
		xslturl = xslt;
	}


	/**
	 * getter for channelid
	 * @return the channelid of the channel
	 */
	public String getChannelid() {
		return channelid;
	}

	/**
	 * getter for channelname.
	 * @return the channel name.
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * getter for the owner name.
	 * @return the owner name of the channel
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * getter for the xpath.
	 * @return the xpath associated with the current channel.
	 */
	public String getXpath() {
		return xpath;
	}

	/**
	 * getters for the xslt url that is to be
	 * used for the channel display
	 * @return
	 */
	public String getXslturl() {
		return xslturl;
	}


	@PrimaryKey
	String channelid;
	String channelName;
	String owner;
	String xpath;
	String xslturl;


}
