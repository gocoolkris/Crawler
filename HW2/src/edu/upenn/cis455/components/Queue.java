package edu.upenn.cis455.components;

import java.util.ArrayList;
import java.util.Vector;

/**
 * the queue class that is used to by the crawler threads to access the url
 * to be downloaded.
 * @author gokul
 *
 */
public class Queue {

	//private static Object lockQueue;

	private static Vector<String> urls;

	public Queue(){
		urls = new Vector<String>();
	}

	/**
	 * the methods enqueues the url that has been extracted.
	 * the method is synchronized to avoid concurreny problems.
	 * @param url
	 * @return
	 */
	public boolean enqueueUrl(String url){
		try{
			synchronized(this){
				if(urls.isEmpty()){
					urls.add(url);
					notifyAll();
					return true;
				}
				urls.add(url);
				return true;
			}
		}
		catch(Exception e){
			return false;
		}
	}

	/**
	 * this method does the same as enqueueUrl except
	 * that it enqueues a list of urls.
	 * @param urlList
	 * @return true if the url has been successfully 
	 * enqueued, false otherwise.
	 */
	public boolean enqueueUrlList(ArrayList<String> urlList){
		try{
			synchronized(this){
				if(urls.isEmpty()){
					for(String url : urlList) urls.add(url);
					notifyAll();
					return true;
				}
				for(String url : urlList) urls.add(url);
				return true;
			}
		}catch(Exception e){
			return false;
		}
	}



	/**
	 * checks whether the queue is empty.
	 * @return true if the queue is empty, false otherwise.
	 */
	public boolean isEmpty(){
		synchronized(this){
			if(urls.isEmpty()) return true;
			else return false;
		}
	}

	/**
	 * this method dequeues the url at the head of the
	 * queue. If the queue is empty, the threads are put to
	 * sleep.
	 * @return the url at the head of the queue.
	 */
	public String dequeueUrl(){
		try{
			synchronized(this){
				if(urls.isEmpty()) wait();
				return urls.remove(0);
			}
		}catch(Exception e){
			return null;
		}
		
	}

}
