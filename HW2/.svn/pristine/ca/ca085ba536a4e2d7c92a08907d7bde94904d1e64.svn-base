package edu.upenn.cis455.components;


public class FilesDownloadedCounter {

	private int filedownloadCount;
	
	/**
	 * public method that keeps counting the number of 
	 * files downloaded currently. Each thread that downloads a
	 * file keeps incrementing the download count.
	 */
	public void incrementCounter(){
		synchronized(this){
			if(filedownloadCount >= GlobalData.fileCrawlingLimit){
				GlobalData.shutdown = true;
			}
			else filedownloadCount++;
		}
	}
	
	/**
	 * @return the number of files downloaded
	 * at the point of time the method was called.
	 */
	public int getDownloadedFilesCount(){
		synchronized(this){
			return filedownloadCount;
		}
	}
	
}
