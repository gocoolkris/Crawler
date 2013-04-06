package edu.upenn.cis455.components;

import java.util.ArrayList;

public class VisitedUrl {

	private static ArrayList<String> visitedUrls = new ArrayList<String>();
	private static Object visitLock = new Object();
	
	/**
	 * the static class is used to book mark the list of urls
	 * that has been crawled in this crawl run.Again the list has
	 * to be synchronized as many threads would be accessing them
	 * simultaneously.
	 * @param url - the url to be checked if it has been already 
	 * crawled.
	 * @return - true it has been already visited, false otherwise.
	 */
	public static boolean isAlreadyCrawled(String url){
		synchronized(visitLock){
			if(visitedUrls.contains(url))
				return true;
			return false;
		}
	}
	
	/**
	 * this method adds the url to the list of urls that
	 * has already been visited.
	 * @param url - the url to be added to the queue.
	 */
	public static void addToCrawledList(String url){
		synchronized(visitLock){
			visitedUrls.add(url);
		}
	}
	
}
