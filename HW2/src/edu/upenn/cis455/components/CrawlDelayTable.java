package edu.upenn.cis455.components;

import java.net.URL;
import java.util.Date;
import java.util.HashSet;

/**
 * @author gokul
 * This class has a subclass - CrawlDelay that stores the value of 
 * crawl delay which is embedded as an object.
 *
 */
public class CrawlDelayTable {


	private static HashSet<CrawlDelay> domainDelayList = new HashSet<CrawlDelay>();
	private static Object crawldelaylock = new Object();


	/**
	 * method to add the crawl delay details to the 
	 * existing array list of domains
	 * @param url - url from which domain is to be extracted.
	 * @param delay - the delay as specified in robots.txt
	 */
	public void addDomainDelay(String url, int delay){
		try{
			URL Url = new URL(url);
			String domainname = Url.getHost();
			synchronized(crawldelaylock){
				CrawlDelay cd = new CrawlDelay(domainname, delay);
				cd.updateNowAsLastAccessed();
				domainDelayList.add(cd);
			}
		}catch(Exception e){}
	}

	/**
	 * checks whether the domain already exists.
	 * @param url - the url from which domain is to be extracted.
	 * @return - true if the domain entry is there, false
	 * otherwise.
	 */
	public boolean hasEntry(String url){
		try{
			URL Url = new URL(url);
			String dmn = Url.getHost();
			synchronized(crawldelaylock){
				for(CrawlDelay cd : domainDelayList){
					if(cd.domain.equals(dmn)) return true;
				}
			}
		}catch(Exception e){}
		return false;
	}

	/**
	 * gets the actual waiting time based on the last access entry
	 * it accounts for the crawldelay from the point where it was 
	 * last accessed.
	 * @param url - the url of the site that will be accessed.
	 * @return the waiting time in milliseconds.
	 */
	public long getWaitingTimeIfAny(String url){
		try{
			URL Url = new URL(url);
			String domainname = Url.getHost();
			synchronized(crawldelaylock){
				for(CrawlDelay cd : domainDelayList){
					if(cd.domain.equals(domainname)){
						long delay = cd.crawldelay * 1000;
						long elapsedTimeSinceLastRequest = (new Date().getTime()) - cd.lastAccessed.getTime();
						if(elapsedTimeSinceLastRequest >= delay) return 0;
						else return (delay - elapsedTimeSinceLastRequest);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * resets the current time to the last access
	 * time of the domain.
	 * @param domainname
	 */
	public void updateNowAsLastAccessedTime(String domainname){
		synchronized(crawldelaylock){
			for(CrawlDelay cd : domainDelayList){
				if(cd.domain.equals(domainname)){
					cd.updateNowAsLastAccessed();
					return;
				}
			}
		}
	}



	/**
	 * class that stores the domain CrawlDelay details.
	 * @author gokul
	 *
	 */
	public class CrawlDelay{

		String domain;
		int crawldelay;
		Date lastAccessed;
		public CrawlDelay(String dom, int delay){
			domain = dom;
			crawldelay = delay;
			lastAccessed = new Date();
		}

		public void updateNowAsLastAccessed(){
			lastAccessed = new Date();
		}

		@Override
		public boolean equals(Object that){
			if(!(that instanceof CrawlDelay)) return false;
			CrawlDelay tht = (CrawlDelay) that;
			if(this.domain.equals(tht.domain)) return true;
			return false;
		}
	}
}
