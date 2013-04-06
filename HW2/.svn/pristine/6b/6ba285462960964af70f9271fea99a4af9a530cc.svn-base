package edu.upenn.cis455.crawler;

import java.util.ArrayList;

import edu.upenn.cis455.components.FilesDownloadedCounter;
import edu.upenn.cis455.components.GlobalData;
import edu.upenn.cis455.components.Queue;
import edu.upenn.cis455.components.VisitedUrl;
import edu.upenn.cis455.httpclient.HttpClient;
import edu.upenn.cis455.validator.DoMTreeBuilder;
import edu.upenn.cis455.xpathengine.XPathEngineImpl;

public class CrawlerThread implements Runnable{

	static Queue urlQueue;
	int threadIndex;

	public CrawlerThread(){}
	public CrawlerThread(int i){
		threadIndex = i;
	}
	
	/**
	 * the crawler threads works as follows
	 * if already downloaded {
	 *  if content-changed-since-lastCrawl
	 *     download new content and update.
	 *  else getLocalCopy
	 *  if html extract links and add to queue
	 *  else if xml validate and update database.       
	 * }
	 * else{
	 *   download new content
	 *   update database.
	 *   if html extract links and add to queue
	 *   elseif xml validate and update database
	 * }
	 */
	@Override
	public void run() {
		while(true){
			try{
				if(GlobalData.shutdown) break;
				ThreadPool.crawlerThreadIsSleeping[threadIndex] = true;
				String url = GlobalData.urlQueue.dequeueUrl();
				if(url == null) continue;
				ThreadPool.crawlerThreadIsSleeping[threadIndex] = false;
				if(VisitedUrl.isAlreadyCrawled(url))
					continue;
				else{
					if(GlobalData.dbCopyOfUrls.containsKey(url)){
						HttpClient client = new HttpClient(url);
						String content = null;
						if(client.checkIfFileModifiedSince(GlobalData.dbCopyOfUrls.get(url))){
							content = client.getDocument().toString();
							GlobalData.serviceProvider.getDocumentService().addDocument(url, content);
						}
						else {
							content = GlobalData.serviceProvider.getDocumentService().getDocumentContent(url);
							GlobalData.counter.incrementCounter();
						}
						if(url.endsWith("xml")){
							for(String channel : GlobalData.channelidXpaths.keySet()){
								String xpathExpression = GlobalData.channelidXpaths.get(channel);
								String[] xpaths = xpathExpression.split(";");
								DoMTreeBuilder tree = new DoMTreeBuilder(content, url);
								XPathEngineImpl xpathimpl = new XPathEngineImpl();
								xpathimpl.setXPaths(xpaths);
								xpathimpl.evaluate(tree.getDocumentNode());
								if(xpathimpl.atLeastOneXpathIsValid()){
									GlobalData.serviceProvider.getCcs().addDocument(channel, url);
								}
								else GlobalData.serviceProvider.getCcs().removeDocument(channel, url);
							}
						}
						else{
							LinkExtractor le = new LinkExtractor(url, content);
							le.parseDocument();
							ArrayList<String> links = le.getLinks();
							GlobalData.urlQueue.enqueueUrlList(links);
						}
					}
					else{
						HttpClient client = new HttpClient(url);
						if(client.isDisallowed()){
							System.out.println("Url Disallowed :" + url );
							continue;
						}
						if(GlobalData.delayTable.hasEntry(url)){
							long time = GlobalData.delayTable.getWaitingTimeIfAny(url);
							if(time > 0) System.out.println("Crawl-Delay found.Waiting");
							Thread.sleep(time);
							if(time > 0) System.out.println("Wait over");
						}else if(client.hasCrawlDelay()){
							long time = client.getCrawlDelay() * 1000;
							if(time > 0) System.out.println("Crawl-Delay found.Waiting");
							Thread.sleep(time);
							if(time > 0) System.out.println("Wait over");
							GlobalData.delayTable.addDomainDelay(url, client.getCrawlDelay());
						}
						client.getHeadRequestResponse();
						if(client.doesFileExceedLimit() || (!client.isFileMimeTypeAcceptable())){
							System.out.println("file size greater than limit or Mime type not relevant");
							System.out.print("and hence not downloading");
							continue;
						}
						System.out.println("Downloading Document");
						String content = client.getDocument().toString();
						System.out.println("Download finished");
						GlobalData.counter.incrementCounter();
						GlobalData.serviceProvider.getDocumentService().addDocument(url, content);
						if(url.endsWith("xml")){
							for(String channel : GlobalData.channelidXpaths.keySet()){
								String xpathExpression = GlobalData.channelidXpaths.get(channel);
								String[] xpaths = xpathExpression.split("\n");
								DoMTreeBuilder tree = new DoMTreeBuilder(content, url);
								XPathEngineImpl xpathimpl = new XPathEngineImpl();
								xpathimpl.setXPaths(xpaths);
								xpathimpl.evaluate(tree.getDocumentNode());
								if(xpathimpl.atLeastOneXpathIsValid()){
									GlobalData.serviceProvider.getCcs().addDocument(channel, url);
									System.out.println("adding documents :" + url);
								}
								else GlobalData.serviceProvider.getCcs().removeDocument(channel, url);
							}
						}
						else{
							LinkExtractor le = new LinkExtractor(url, content);
							le.parseDocument();
							ArrayList<String> links = le.getLinks();
							GlobalData.urlQueue.enqueueUrlList(links);
						}
					}

				}
				VisitedUrl.addToCrawledList(url);
			}catch(Exception e){
				if(GlobalData.shutdown)break;
				e.printStackTrace();
			}
		}
	}




}
