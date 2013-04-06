package edu.upenn.cis455.crawler;

import edu.upenn.cis455.components.GlobalData;

/**
 * the threadpool for running the crawling concurrently.
 * @author gokul
 *
 */
public class ThreadPool {

	static Thread[] pool = null;
	public static boolean[] crawlerThreadIsSleeping;
	
	/**
	 * the constructor initializes a set of threads 
	 * that needs to be run for concurrent crawling.
	 */
	public ThreadPool(){
		
		pool = new Thread[GlobalData.CRAWLER_THREADCOUNT];
		crawlerThreadIsSleeping = new boolean[GlobalData.CRAWLER_THREADCOUNT];
		for(int i = 0; i < pool.length; ++i){
			pool[i] = new Thread(new CrawlerThread(i));
			crawlerThreadIsSleeping[i] = true;
		}
	}
	
	/**
	 * helper method that starts the threadpool. This will instantiate
	 * a fixed number of threads that keeps accessing and crawling 
	 * concurrently.
	 */
	public void startThreadPool(){
		for(int i = 0; i < GlobalData.CRAWLER_THREADCOUNT; ++i){
			pool[i].start();
		}
	}
	
	/**
	 * @return true if any of the threads of the Thread pool is alive.
	 */
	public boolean threadsAlive(){
		for(int i = 0; i < GlobalData.CRAWLER_THREADCOUNT; ++i){
			if(pool[i].isAlive()) return true;
		}
		return false;
	}
	
	/**
	 * checks whether all the threads are sleeping. this helper
	 * method is used to shutdown when the crawling is complete.
	 * @return
	 */
	public boolean allThreadsSleeping(){
		for(int i = 0; i < GlobalData.CRAWLER_THREADCOUNT; ++i){
			if(!crawlerThreadIsSleeping[i]) return false;
		}
		return true;
	}
	
	
	/**
	 * this helper method actually shuts down the thread pool.
	 * waits untill all the threads are shut down.
	 * @return
	 */
	public boolean shutdownThreadPool(){
		try{
			System.out.println("shudownCommandReceived");
			for(int i = 0; i < GlobalData.CRAWLER_THREADCOUNT; ++i){
				pool[i].interrupt();
			}
			while(threadsAlive());
			System.out.println("Threadpool shutdown");
			return true;
			}
			catch(Exception e){return false;}
		}
	
	
}
